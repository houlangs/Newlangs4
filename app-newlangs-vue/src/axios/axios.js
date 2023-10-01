import axios from 'axios'

const axiosIns = axios.create({
  baseURL: '', // 你的后端API地址
  timeout: 20000
})
axiosIns.interceptors.request.use(function (config) {
  let accessToken = localStorage.getItem('accessToken')

  config.headers = {
    'Content-Type': 'application/json;charset=UTF-8',
    'accessToken': accessToken
  }
  return config
}, function (error) {
  return Promise.reject(error)
})
axiosIns.interceptors.response.use(
  function (response) {
    let message = String(response.data.msg)
    if (message.includes('未能读取到有效 token') || message.includes('token 无效')) {
      window.location.href = '/#/auth/login'
    }
    return response
  }, function (error) {
    return Promise.reject(error)
  })
export default axiosIns