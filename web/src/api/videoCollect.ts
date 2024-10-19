import { get, post } from '/@/utils/http/axios';

enum URL {
    userCollectList = '/api/videoCollect/getUserCollectList',
    collect = '/api/videoCollect/collect',
    unCollect = '/api/videoCollect/unCollect',
}

// 默认headers配置
const defaultHeaders = {
    'Content-Type': 'multipart/form-data;charset=utf-8'
};

// 简化API方法定义
const apiPost = async (url: string, data: any = {}, params: any = {}, headers: any = defaultHeaders) =>
    post<any>({ url, data, params, headers });

const apiGet = async (url: string, params: any = {}, headers: any = {}) =>
    get<any>({ url, params, headers });

// API方法
const collectApi = (data: any) => apiPost(URL.collect, data);
const unCollectApi = (params: any) => apiPost(URL.unCollect, {}, params);
const userCollectListApi = (params: any) => apiGet(URL.userCollectList, params);

export { collectApi, unCollectApi, userCollectListApi };
