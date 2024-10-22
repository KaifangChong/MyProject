import {get} from '/@/utils/http/axios';

enum URL {
    list = '/api/overview/count',
}

// 简化API方法定义
const apiGet = async (url: string, params: any = {}) =>
    get<any>({url, params});

// API方法
const listApi = (params: any) => apiGet(URL.list, params);

export {listApi};
