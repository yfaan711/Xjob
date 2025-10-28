// 用户相关API服务

/**
 * 发送手机验证码
 * @param {string} phone - 手机号
 * @returns {Promise<Object>} 响应数据
 */
export const sendCode = async (phone) => {
  try {
    console.log('发送验证码请求:', phone);
    const response = await fetch(`/api/user/code?phone=${phone}`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json'
      },
      credentials: 'include'
    });
    
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.errorMsg || `发送验证码失败，状态码: ${response.status}`);
    }
    
    const data = await response.json();
    console.log('验证码响应:', data);
    return data;
  } catch (error) {
    console.error('发送验证码失败:', error);
    // 确保返回标准格式的错误对象，方便前端统一处理
    throw {
      success: false,
      errorMsg: error.message || '发送验证码失败，请稍后重试',
      data: null
    };
  }
};

/**
 * 用户登录
 * @param {Object} loginForm - 登录表单数据
 * @param {string} loginForm.phone - 手机号
 * @param {string} [loginForm.code] - 验证码（验证码登录时使用）
 * @param {string} [loginForm.password] - 密码（密码登录时使用）
 * @returns {Promise<Object>} 响应数据
 */
export const login = async (loginForm) => {
  try {
    console.log('发送登录请求:', loginForm);
    const response = await fetch('/api/user/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(loginForm),
      credentials: 'include'
    });
    
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.errorMsg || `登录请求失败，状态码: ${response.status}`);
    }
    
    const data = await response.json();
    console.log('登录响应:', data);
    return data;
  } catch (error) {
    console.error('登录失败:', error);
    // 确保返回标准格式的错误对象，方便前端统一处理
    throw {
      success: false,
      errorMsg: error.message || '登录失败，请稍后重试',
      data: null
    };
  }
};

/**
 * 用户登出
 * @returns {Promise<Object>} 响应数据
 */
export const logout = async () => {
  try {
    const response = await fetch('/api/user/logout', {
      method: 'POST',
      headers: {
        'Accept': 'application/json'
      },
      credentials: 'include'
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('登出失败:', error);
    throw error;
  }
};

/**
 * 获取当前登录用户信息
 * @returns {Promise<Object>} 用户信息
 */
export const getCurrentUser = async () => {
  try {
    const response = await fetch('/api/user/me', {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      },
      credentials: 'include'
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
};

/**
 * 获取用户详细信息
 * @param {number} userId - 用户ID
 * @returns {Promise<Object>} 用户详细信息
 */
export const getUserInfo = async (userId) => {
  try {
    const response = await fetch(`/api/user/info/${userId}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      },
      credentials: 'include'
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('获取用户详情失败:', error);
    throw error;
  }
};