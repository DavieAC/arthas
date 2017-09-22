package com.jd.arthas.common.test;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

// ChannelInboundHandlerAdapter实现自ChannelInboundHandler
// ChannelInboundHandler提供了不同的事件处理方法你可以重写
@SuppressWarnings("deprecation")
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /*
     * @作者:CJY
     * 
     * @说明:该方法用于接收从客户端接收的信息
     * 
     * @时间:2017-4-2下午12:25:05
     * 
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.
     * ChannelHandlerContext, java.lang.Object)
     * 
     * @param ctx
     * 
     * @param msg
     * 
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Discard the received data silently
        // ByteBuf是一个引用计数对象实现ReferenceCounted，他就是在有对象引用的时候计数+1，无的时候计数-1，当为0对象释放内存
        ByteBuf in = (ByteBuf) msg;
        try {
            if (in.isReadable()) {
                String str = in.toString(Charset.forName("UTF-8"));
                System.out.println(str);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
