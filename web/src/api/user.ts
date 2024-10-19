import { get, post } from '/@/utils/http/axios';

enum URL {
    login = '/api/user/login',
    userList = '/api/user/list',
    detail = '/api/user/detail',
    create = '/api/user/create',
    update = '/api/user/update',
    delete = '/api/user/delete',
    userLogin = '/api/user/userLogin',
    userRegister = '/api/user/userRegister',
    updateUserPwd = '/api/user/updatePwd',
    updateUserInfo = '/api/user/updateUserInfo'
}

export interface LoginData {
    username: string;
    password: string;
}

// 默认headers配置
const defaultHeaders = {
    'Content-Type': 'multipart/form-data;charset=utf-8'
};

// 简化API方法定义
const apiPost = async (url: string, data: any = {}, headers: any = defaultHeaders, params: any = {}) =>
    post<any>({ url, data, headers, params });

const apiGet = async (url: string, params: any = {}, headers: any = {}, data: any = {}) =>
    get<any>({ url, params, headers, data });

// API方法
const loginApi = (data: LoginData) => apiPost(URL.login, data);
const listApi = (params: any) => apiGet(URL.userList, params);
const detailApi = (params: any) => apiGet(URL.detail, params);
const createApi = (data: any) => apiPost(URL.create, data);
const updateApi = (data: any) => apiPost(URL.update, data);
const deleteApi = (params: any) => apiPost(URL.delete, {}, {}, params);
const userLoginApi = (data: LoginData) => apiPost(URL.userLogin, data);
const userRegisterApi = (data: any) => apiPost(URL.userRegister, data);
const updateUserPwdApi = (params: any) => apiPost(URL.updateUserPwd, {}, {}, params);
const updateUserInfoApi = (data: any) => apiPost(URL.updateUserInfo, data);

export {
    loginApi,
    listApi,
    detailApi,
    createApi,
    updateApi,
    deleteApi,
    userLoginApi,
    userRegisterApi,
    updateUserPwdApi,
    updateUserInfoApi
};
