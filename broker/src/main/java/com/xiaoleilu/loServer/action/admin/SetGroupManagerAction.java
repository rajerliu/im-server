/*
 * This file is part of the Wildfire Chat package.
 * (c) Heavyrain2012 <heavyrain.lee@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.xiaoleilu.loServer.action.admin;


import cn.wildfirechat.common.APIPath;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.pojos.InputSetGroupManager;
import com.google.gson.Gson;
import com.xiaoleilu.loServer.RestResult;
import com.xiaoleilu.loServer.annotation.HttpMethod;
import com.xiaoleilu.loServer.annotation.Route;
import com.xiaoleilu.loServer.handler.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import win.liyufan.im.IMTopic;

@Route(APIPath.Group_Set_Manager)
@HttpMethod("POST")
public class SetGroupManagerAction extends AdminAction {

    @Override
    public boolean isTransactionAction() {
        return true;
    }

    @Override
    public boolean action(Request request) {
        if (request.getNettyRequest() instanceof FullHttpRequest) {
            InputSetGroupManager inputAddGroupMember = getRequestBody(request.getNettyRequest(), InputSetGroupManager.class);
            if (inputAddGroupMember.isValide()) {
                sendApiMessage(inputAddGroupMember.getOperator(), IMTopic.SetGroupManagerTopic, inputAddGroupMember.toProtoGroupRequest().toByteArray(), result -> {
                    ByteBuf byteBuf = Unpooled.buffer();
                    byteBuf.writeBytes(result);
                    ErrorCode errorCode = ErrorCode.fromCode(byteBuf.readByte());
                    return new Result(errorCode);
                });
                return false;
            } else {
                setResponseContent(RestResult.resultOf(ErrorCode.INVALID_PARAMETER));
            }
        }
        return true;
    }
}
