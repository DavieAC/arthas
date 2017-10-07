package com.arthas.rpc.net.nio.test.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arthas.common.constant.Constant;



public class NioServer {

    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);

    private Selector selector;

    private ServerSocketChannel serverChannel;

    private volatile boolean stop;

    private int port;

    /**
     * 设置本机端口号
     * @param port
     */
    public NioServer(int port) {
        this.port = port;
    }

    /**
     * 
     * @Title: start
     * @Description: 开启本机端口监听
     * @throws
     */
    public void start() {
        try {
            selector = Selector.open();
            /*
             * ServerSocketChannel 的顶层接口是:SelectableChannel 而SelectableChannel
             * 可以理解为一个"可复用连接"的抽象.其通过注册一个selector来实现复用.
             */
            serverChannel = ServerSocketChannel.open();
            // 非阻塞式
            serverChannel.configureBlocking(false);
            // 绑定端口和最大连接数
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 注册一个selector
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info(String.format("开启本机:%s端口", port));
            
            // 同步接受请求
            handleRequest();
        } catch (IOException e) {
            logger.error("启动本机nio服务端异常", e);
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void handleRequest() {
        while (!stop) {
            try {
                /*
                 * 该方法阻塞,直到至少一个channel准备好I/O操作(或者超时时间到,或者被打断)
                 * 这里不设置为0(如果没有I/O操作准备好,或者没被打断会一直阻塞下去)的理由是:需要定时检测stop标志位
                 */
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    // help GC
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        // 如果接到处理一个socket请求异常时,尝试关闭这个链接
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Nio服务器运行异常", e);
            }
        }

        // 关闭selector
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                logger.error("selector 关闭异常", e);
            }
        }

    }

    private void handleInput(SelectionKey key) throws IOException {

        // 如果key被取消,连接被关闭,或者selector被关闭,这个链接不需要处理
        if (!key.isValid()) {
            return;
        }

        // 检测是否支持建立socket连接
        if (key.isAcceptable()) {
            // 还原为服务端的连接
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            // 建立连接(相当于TCP 三次握手成功)
            SocketChannel sc = ssc.accept();
            // 理论上不应该为null
            if (sc != null) {
                // 本链接为非阻塞式
                sc.configureBlocking(false);
                // 有半包读的可能性,所以要把这个channel注册到selector中,有传输会被读取到
                sc.register(selector, SelectionKey.OP_READ);
            }
        }

        // 检查本链接是否已经进入了可读状态(网络数据传输完毕,并且从系统空间移入用户空间)
        if (key.isReadable()) {

            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

            // 非阻塞式可能出现不完整数据问题,这里获取本次读取的字节数,同时把数据读入到 readBuffer
            int readBytes = sc.read(readBuffer);

            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                // 数据写入到byte型数组中
                readBuffer.get(bytes);
                // 转换为String
                String body = new String(bytes, Constant.UTF_8);
                logger.info("服务器接受到指令:{}", body);

                // 使用终端会导致最后有"\r\n" 所以采取匹配策略
                if (body.indexOf("exit") != -1) {

                    logger.info("关闭本次链接");
                    doWrite(sc, "关闭本次链接\n");
                    sc.close();
                    return;
                }

                // 回复
                doWrite(sc, String.format("服务端已经接受到了指令:%s\n", body));
            } else if (readBytes < 0) {
                // 对端链路关闭
                key.cancel();
                sc.close();
            } else {
                // 读到0字节 忽略
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {

        if (response == null || response.isEmpty()) {
            return;
        }

        // 把回应按照UTF-8的形式拆解为字节
        byte[] bytes = response.getBytes(Constant.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        // 设置读写指针
        buffer.flip();
        channel.write(buffer);
    }

    public static void main(String[] args) {
        int port = 8088;
        NioServer server = new NioServer(port);
        server.start();
    }

}
