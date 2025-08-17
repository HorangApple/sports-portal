/**
 * API 서비스 레이어
 * 
 * 백엔드 API와의 통신을 담당하는 서비스 함수들을 제공합니다.
 */

import axios from 'axios';

// API 기본 URL 설정
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8000';

// Axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// 요청 인터셉터 - 인증 토큰 추가
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 인증 관련 API
export const authAPI = {
  login: async (credentials) => {
    try {
      console.log('로그인 요청 시작:', credentials);
      const response = await apiClient.post('/api/v1/auth/login', credentials);
      console.log('로그인 응답 수신:', response.data);
      
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        console.log('토큰 저장 완료:', response.data.token);
      } else if (response.data.accessToken) {
        // 일부 백엔드는 accessToken이라는 이름으로 토큰을 제공
        localStorage.setItem('token', response.data.accessToken);
        console.log('accessToken 저장 완료:', response.data.accessToken);
      } else {
        console.warn('응답에 토큰이 없습니다:', response.data);
      }
      
      return response.data;
    } catch (error) {
      console.error('로그인 실패:', error);
      console.error('에러 응답:', error.response?.data);
      throw error;
    }
  },
  
  register: async (userData) => {
    try {
      const response = await apiClient.post('/api/v1/users/register', userData);
      return response.data;
    } catch (error) {
      console.error('Registration failed:', error);
      throw error;
    }
  },
  
  logout: () => {
    localStorage.removeItem('token');
  },
  
  getCurrentUser: async () => {
    try {
      const response = await apiClient.get('/api/v1/auth/me');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch current user:', error);
      throw error;
    }
  }
};

// 사용자 학습 관련 API
export const learningAPI = {
  // 진행 중인 코스 목록 조회
  getInProgressCourses: async () => {
    try {
      const response = await apiClient.get('/api/v1/user/courses/in-progress');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch in-progress courses:', error);
      throw error;
    }
  },

  // 완료한 코스 목록 조회
  getCompletedCourses: async () => {
    try {
      const response = await apiClient.get('/api/v1/user/courses/completed');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch completed courses:', error);
      throw error;
    }
  },

  // 저장한 코스 목록 조회
  getSavedCourses: async () => {
    try {
      const response = await apiClient.get('/api/v1/user/courses/saved');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch saved courses:', error);
      throw error;
    }
  },

  // 코스 저장/저장 취소
  toggleSaveCourse: async (courseId, isSaving = true) => {
    try {
      if (isSaving) {
        const response = await apiClient.post('/api/v1/user/courses/save', { courseId });
        return response.data;
      } else {
        const response = await apiClient.delete(`/api/v1/user/courses/save/${courseId}`);
        return response.data;
      }
    } catch (error) {
      console.error(`Failed to ${isSaving ? 'save' : 'unsave'} course:`, error);
      throw error;
    }
  },

  // 학습 통계 조회
  getLearningStats: async () => {
    try {
      const response = await apiClient.get('/api/v1/user/learning-stats');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch learning stats:', error);
      throw error;
    }
  },

  // 코스 수강 신청
  enrollCourse: async (courseId, applyReason = '') => {
    try {
      const response = await apiClient.post('/api/v1/user/courses/enroll', {
        courseId,
        applyReason
      });
      return response.data;
    } catch (error) {
      console.error('Failed to enroll in course:', error);
      throw error;
    }
  },

  // 수강 신청 취소
  cancelEnrollment: async (enrollmentId, reason = '') => {
    try {
      const response = await apiClient.delete(`/api/v1/user/courses/${enrollmentId}`, {
        params: { reason }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to cancel enrollment:', error);
      throw error;
    }
  }
};

// 코스 관련 API
export const courseAPI = {
  // 사용자 맞춤 추천 과정 목록 조회
  getPersonalizedCourses: async (limit = 5) => {
    try {
      const response = await apiClient.get('/api/v1/courses/recommendations/personalized', {
        params: { limit }
      });
    } catch (error) {
      console.error('Failed to fetch personalized courses:', error);
    }
  },

  // 모든 추천 과정 목록 조회
  getAllRecommendations: async (limit = 3) => {
    try {
      const response = await apiClient.get('/api/v1/courses/recommendations', {
        params: { limit }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch all recommendations:', error);
      throw error;
    }
  },

  // 캘린더 일정 조회
  getCalendarEvents: async (params = {}) => {
    try {
      const response = await apiClient.get('/api/v1/courses/schedules', { params });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch calendar events:', error);
      // 백엔드 API가 준비되지 않은 경우 임시 데이터 반환
      if (error.response && error.response.status === 404) {
        console.warn('캘린더 API 엔드포인트가 구현되지 않았습니다. 임시 데이터를 사용합니다.');
        return {
          success: true,
          data: [] // 백엔드에서 실제 데이터를 가져올 수 있을 때까지 빈 배열 반환
        };
      }
      throw error;
    }
  }
};


// 명명된 기본 내보내기로 수정
const apiService = {
  auth: authAPI,
  learning: learningAPI
};

export default apiService; 