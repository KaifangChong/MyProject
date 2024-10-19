import { get } from '/@/utils/http/axios';

enum URL {
    loginLogList = '/api/opLog/loginLogList',
    opLogList = '/api/opLog/list',
    errorLogList = '/api/errorLog/list',
}

// 简化API方法定义
const apiGet = async (url: string, params: any = {}) =>
    get<any>({ url, params });

// API方法
const listLoginLogApi = (params: any) => apiGet(URL.loginLogList, params);
const listOpLogListApi = (params: any) => apiGet(URL.opLogList, params);
const listErrorLogListApi = (params: any) => apiGet(URL.errorLogList, params);

export { listLoginLogApi, listOpLogListApi, listErrorLogListApi };
