import { get, post } from '/@/utils/http/axios';

enum URL {
    list = '/api/comment/list',
    create = '/api/comment/create',
    delete = '/api/comment/delete',
    listVideoComments = '/api/comment/listVideoComments',
    listUserComments = '/api/comment/listUserComments',
    like = '/api/comment/like'
}

// 简化API方法定义
const apiPost = async (url: string, data: any = {}, params: any = {}, headers: any = {'Content-Type': 'multipart/form-data;charset=utf-8'}) =>
    post<any>({ url, data, params, headers });

const apiGet = async (url: string, params: any = {}, headers: any = {}) =>
    get<any>({ url, params, headers });

// API方法
const listApi = (params: any) => apiGet(URL.list, params);
const createApi = (data: any) => apiPost(URL.create, data);
const deleteApi = (params: any) => apiPost(URL.delete, {}, params, {});
const listVideoCommentsApi = (params: any) => apiGet(URL.listVideoComments, params);
const listUserCommentsApi = (params: any) => apiGet(URL.listUserComments, params);
const likeApi = (params: any) => apiPost(URL.like, {}, params, {});

export { listApi, createApi, deleteApi, listVideoCommentsApi, listUserCommentsApi, likeApi };
