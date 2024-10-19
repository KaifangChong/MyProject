import { get, post } from '/@/utils/http/axios';

enum URL {
    list = '/api/video/list',
    create = '/api/video/create',
    update = '/api/video/update',
    delete = '/api/video/delete',
    detail = '/api/video/detail',
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
const listApi = (params: any) => apiGet(URL.list, params);
const createApi = (data: any) => apiPost(URL.create, data);
const updateApi = (data: any) => apiPost(URL.update, data);
const deleteApi = (params: any) => apiPost(URL.delete, {}, params, {});
const detailApi = (params: any) => apiGet(URL.detail, params);

export { listApi, createApi, updateApi, deleteApi, detailApi };
