package ${package}

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * NetManager
 */
public class NetManager {

    /**
     * append requestParams
     */
    private void appendCommParams(Map<String, Object> params) {
        <#list request.fields as param>
        params.put("${param.field}", ${param.field});
        </#list>
    }

    /**
     * ${svcName}
     * ${svcCaption}
     * ${mode}
     * ${target}
     * ${comments}
     */
    public Observable<Object> ${svcName}() {
        Map<String, Object> params = new HashMap<>();
        appendCommParams(params);
        return ZRRetrofit.getNetApiInstance().pushMsg(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

/********************Below is request params object start************************************/
package ${package}

/**
 * Request Params
 * Request ${request.paramDataType}
 */
public class RequestParams {
    <#list request.fields as param>

    /**
     * ${param.caption}
     *
     * field:${param.field}
     * fieldSafeName:${param.fieldSafeName}
     * caption:${param.caption}
     * fieldType:${param.fieldType}
     * specifyDefault:${param.specifyDefault}
     */
    public ${param.fieldType} ${param.field};
    </#list>
}
/********************request params object end************************************/

/********************Below is response params object start************************************/
package ${package}

import java.io.Serializable;

/**
 * Net Response Bean
 */
public class RspBaseData implements Serializable {
    <#list response.fields as param>

    /**
     * ${param.caption}
     */
    public ${param.fieldType} ${param.field};
    </#list>
    <#list response.subModels as subModel>

    /**
     * ${subModel.modelName}
     */
    public class Rsp${subModel.modelName} implements Serializable {
        <#list subModel.modelName.fields as param>

        /**
         * ${param.caption}
         */
        private ${param.fieldType} ${param.field};
        </#list>
    }
    </#list>
}
/********************response params object end************************************/