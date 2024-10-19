import { get, post } from '/@/utils/http/axios';

enum URL {
    userWishList = '/api/videoWish/getUserWishList',
    wish = '/api/videoWish/wish',
    unWish = '/api/videoWish/unWish',
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
const wishApi = (data: any) => apiPost(URL.wish, data);
const unWishApi = (params: any) => apiPost(URL.unWish, {}, params);
const userWishListApi = (params: any) => apiGet(URL.userWishList, params);

export { wishApi, unWishApi, userWishListApi };
