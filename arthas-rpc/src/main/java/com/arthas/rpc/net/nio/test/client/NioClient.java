package com.arthas.rpc.net.nio.test.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arthas.common.constant.Constant;

public class NioClient {

    private Logger logger = LoggerFactory.getLogger(NioClient.class);

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    public NioClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            logger.error("启动客户端异常", e);
            System.exit(1);
        }
    }

    /**
     * 
     * @Title: connect
     * @Description: 进行连接,首先进行第一次,然后通过selector处理半包写入,或者对回应进行处理
     * @throws
     */
    public void connect() {
        try {
            doConnect();
        } catch (IOException e) {
            logger.error("客户端连接异常", e);
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) key.channel().close();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("客户端连接异常", e);
                System.exit(1);
            }
        }

        // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                logger.error("客户端关闭异常", e);
            }
        }
    }

    /**
     * 
     * @Title: disconnect
     * @Description: 断开连接 原子操作
     * @throws
     */
    public void disconnect() {
        stop = true;
    }

    private void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            // 判断是否连接成功
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    String info = String.format("客户端发送当前时间:%s", new Date().toString());
                    doWrite(sc, info);
                } else {
                    System.exit(1);// 连接失败，进程退出
                }
            }

            // 如果接到了回应
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, Constant.UTF_8);
                    logger.info("客户端接收到:{}", body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                } else {
                    // 读到0字节忽略
                }
            }
        }

    }

    private void doConnect() throws IOException {
        // 如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel, "连接成功");
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    /**
     * 
     * @Title: doWrite
     * @Description: 发送信息,可能有半包写的问题
     * @param sc
     * @throws IOException
     * @throws
     */
    private void doWrite(SocketChannel sc, String info) throws IOException {

        if (info == null || info.isEmpty()) {
            return;
        }

        byte[] req = info.getBytes(Constant.UTF_8);
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            logger.info("客户端发送成功");
        }
    }

    public static void main(String[] args) {
        NioClient client = new NioClient("127.0.0.1", 8088);
        client.connect();

        // 这里只是断开了本地selector,没有断开连接本身
        client.disconnect();
    }

}
