import { defineStore } from 'pinia';
import {loginApi as adminLogin, userLoginApi} from '/@/api/user';
import { UserState } from './types';
import {
  USER_ID,
  USER_NAME,
  USER_TOKEN,
  USER_AVATAR,
  ADMIN_USER_ID,
  ADMIN_USER_NAME,
  ADMIN_USER_TOKEN,
  BASE_URL,
} from "/@/store/constants";

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    user_id: undefined,
    user_name: undefined,
    user_token: undefined,
    user_avatar: undefined,

    admin_user_id: undefined,
    admin_user_name: undefined,
    admin_user_token: undefined,
  }),
  getters: {},
  actions: {
    // 用户登录
    async login(loginForm) {
      const result = await userLoginApi(loginForm);
      console.log('result==>', result)

      if(result.code === 200) {
        this.$patch((state)=>{
          state.user_id = result.data.id
          state.user_name = result.data.username
          state.user_token = result.data.token
          state.user_avatar = BASE_URL + '/api/staticfiles/avatar/' + result.data.avatar
          console.log('state==>', state)
        })

        localStorage.setItem(USER_TOKEN, result.data.token)
        localStorage.setItem(USER_NAME, result.data.username)
        localStorage.setItem(USER_ID, result.data.id)
        localStorage.setItem(USER_AVATAR, BASE_URL + '/api/staticfiles/avatar/' + result.data.avatar)
      }

      return result;
    },
    // 用户登出
    async logout() {
      // await userLogout();
      this.$patch((state)=>{
        localStorage.removeItem(USER_ID)
        localStorage.removeItem(USER_NAME)
        localStorage.removeItem(USER_TOKEN)
        localStorage.removeItem(USER_AVATAR)

        state.user_id = undefined
        state.user_name = undefined
        state.user_token = undefined
        state.user_avatar = undefined
      })
    },

    // 管理员登录
    async adminLogin(loginForm) {
      const result = await adminLogin(loginForm);
      console.log('result==>', result)

      if(result.code === 200) {
        this.$patch((state)=>{
          state.admin_user_id = result.data.id
          state.admin_user_name = result.data.username
          state.admin_user_token = result.data.token
          console.log('state==>', state)
        })

        localStorage.setItem(ADMIN_USER_TOKEN, result.data.token)
        localStorage.setItem(ADMIN_USER_NAME, result.data.username)
        localStorage.setItem(ADMIN_USER_ID, result.data.id)
      }

      return result;
    },
    // 管理员登出
    async adminLogout() {
      // await userLogout();
      this.$patch((state)=>{
        localStorage.removeItem(ADMIN_USER_ID)
        localStorage.removeItem(ADMIN_USER_NAME)
        localStorage.removeItem(ADMIN_USER_TOKEN)

        state.admin_user_id = undefined
        state.admin_user_name = undefined
        state.admin_user_token = undefined
      })
    },
  },
});
