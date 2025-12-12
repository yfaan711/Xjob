// 获取用户token
const getToken = () => {
  return localStorage.getItem('token')
}

// 创建请求头配置
const createHeaders = () => {
  const token = getToken()
  return {
    'Content-Type': 'application/json',
    'authorization': token
  }
}

// 基础请求函数
const request = async (url, options = {}) => {
  const headers = createHeaders()
  const config = {
    ...options,
    headers: {
      ...headers,
      ...options.headers
    }
  }

  try {
    const response = await fetch(url, config)
    
    // 检查响应是否为空
    const contentType = response.headers.get('content-type')
    let responseData;
    if (contentType && contentType.includes('application/json')) {
      responseData = await response.json()
    } else {
      responseData = { data: null }
    }
    
    // 检查响应状态和业务逻辑错误
    if (!response.ok) {
      // HTTP错误状态，抛出完整的响应数据以便上层处理
      const error = new Error(`HTTP error! status: ${response.status}`)
      error.response = { status: response.status, data: responseData }
      throw error
    }
    
    // 检查业务逻辑错误（如success为false的情况）
    if (responseData && responseData.success === false) {
      // 业务逻辑错误，抛出完整的响应数据以便上层处理
      const error = new Error('Business logic error')
      error.response = { status: 200, data: responseData } // HTTP状态码是200，但业务逻辑失败
      throw error
    }
    
    return responseData
  } catch (error) {
    console.error('API请求失败:', error)
    throw error
  }
}

// 获取工作列表（使用后端Userlist接口）
export const getJobs = async () => {
  return request('/api/job/Userlist')
}

// 创建新业务（使用后端add接口）
export const createJob = async (jobData) => {
  // 转换字段名以匹配后端SQL表结构
  const formattedJobData = {
    job: jobData.title, // 将jobName改为job
    skill: Array.isArray(jobData.skills) ? JSON.stringify(jobData.skills) : '[]',
    introduce: jobData.description,
    typeId: jobData.typeId,
    workDuration: jobData.experience !== undefined && jobData.experience !== null ? parseInt(jobData.experience) : 0,
    workCity: Array.isArray(jobData.cities) ? JSON.stringify(jobData.cities) : '[]', // 将work_city改为workCity
    price: jobData.price !== undefined && jobData.price !== null ? parseInt(jobData.price) : 0
  }
  
  return request('/api/job/add', {
    method: 'POST',
    body: JSON.stringify(formattedJobData)
  })
}

// 更新业务（使用后端update接口）
export const updateJob = async (jobData) => {
  if (!jobData.id) {
    throw new Error('缺少业务ID')
  }
  
  // 转换字段名以匹配后端SQL表结构
  const formattedJobData = {
    id: jobData.id,
    job: jobData.title, // 将jobName改为job
    skill: Array.isArray(jobData.skills) ? JSON.stringify(jobData.skills) : '[]',
    introduce: jobData.description,
    typeId: jobData.typeId,
    workDuration: jobData.experience !== undefined && jobData.experience !== null ? parseInt(jobData.experience) : 0,
    workCity: Array.isArray(jobData.cities) ? JSON.stringify(jobData.cities) : '[]', // 将work_city改为workCity
    price: jobData.price !== undefined && jobData.price !== null ? parseInt(jobData.price) : 0
  }
  
  return request('/api/job/update', {
    method: 'POST',
    body: JSON.stringify(formattedJobData)
  })
}

// 删除工作（使用后端delete接口）
export const deleteJob = async (jobId) => {
  if (!jobId) {
    throw new Error('缺少工作ID')
  }
  return request(`/api/job/delete?jobId=${jobId}`, {
    method: 'DELETE'
  })
}

// 获取单个工作详情
export const getJobById = async (jobId) => {
  if (!jobId) {
    throw new Error('缺少工作ID')
  }
  return request(`/api/job/${jobId}`)
}

// 分页获取所有工作列表
export const getAllJobs = async (current = 1, size = 10, typeId = null) => {
  const typeQuery = typeId ? `&typeId=${typeId}` : ''
  return request(`/api/job/page?current=${current}&size=${size}${typeQuery}`)
}

// 获取所有职位类型
export const getJobTypes = async () => {
  return request('/api/jobType/list')
}
