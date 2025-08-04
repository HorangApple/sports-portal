/**
 * 인증 상태 관리 Context
 * 
 * 전체 애플리케이션에서 사용자 인증 상태를 관리하는 컨텍스트
 * 로그인, 로그아웃, 사용자 정보 관리 등의 기능 제공
 */

import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';

// 인증 컨텍스트 생성
const AuthContext = createContext(null);

/**
 * 인증 컨텍스트 제공자 컴포넌트
 * 
 * @param {Object} props - 컴포넌트 프롭스
 * @param {React.ReactNode} props.children - 자식 컴포넌트
 */
export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 로그인 상태 확인 및 사용자 정보 로드
  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        const token = localStorage.getItem('token');
        if (token) {
          // 로컬 스토리지에서 사용자 데이터를 먼저 확인
          const storedUserData = localStorage.getItem('userData');
          if (storedUserData) {
            // 로컬에 저장된 사용자 정보가 있으면 먼저 설정
            setCurrentUser(JSON.parse(storedUserData));
          }

          // 토큰이 있으면 사용자 정보 로드 (API 호출)
          try {
            const userData = await authAPI.getCurrentUser();
            setCurrentUser(userData);
            
            // 최신 사용자 정보로 로컬 스토리지 업데이트
            localStorage.setItem('userData', JSON.stringify(userData));
          } catch (apiErr) {
            console.error('사용자 정보 로드 실패:', apiErr);
            // API 호출 실패해도 토큰과 기본 정보가 있으면 로그인 상태 유지 
            if (!storedUserData) {
              // 저장된 사용자 정보도 없는 경우만 로그아웃
              authAPI.logout();
              setCurrentUser(null);
            }
          }
        }
      } catch (err) {
        console.error('인증 상태 확인 실패:', err);
        // 오류 발생 시 로그아웃 처리
        authAPI.logout();
        localStorage.removeItem('userData');
        setCurrentUser(null);
      } finally {
        setLoading(false);
      }
    };

    checkAuthStatus();
  }, []);

  /**
   * 로그인 함수
   * 
   * @param {string} email - 사용자 이메일
   * @param {string} password - 사용자 비밀번호
   * @returns {Promise} 로그인 결과
   */
  const login = async (email, password) => {
    setError(null);
    try {
      const response = await authAPI.login({ email, password });
      
      // 토큰이 응답에 있는지 확인하고 localStorage에 저장
      if (response.token || response.accessToken) {
        const token = response.token || response.accessToken;
        localStorage.setItem('token', token);
        console.log('토큰이 저장되었습니다:', token);
        
        // 사용자 기본 정보 저장
        const userInfo = {
          id: response.userId,
          email: response.email,
          name: response.name,
          role: response.role
        };
        localStorage.setItem('userData', JSON.stringify(userInfo));
        console.log('사용자 정보가 저장되었습니다');
      } else {
        console.warn('응답에 토큰이 없습니다:', response);
      }
      
      setCurrentUser(response.user || {
        id: response.userId,
        email: response.email,
        name: response.name,
        role: response.role
      });
      return response;
    } catch (err) {
      console.error('로그인 실패:', err);
      setError(err.response?.data?.message || '로그인에 실패했습니다.');
      throw err;
    }
  };

  /**
   * 회원가입 함수
   * 
   * @param {Object} userData - 사용자 등록 정보
   * @returns {Promise} 회원가입 결과
   */
  const register = async (userData) => {
    setError(null);
    try {
      const response = await authAPI.register(userData);
      return response;
    } catch (err) {
      console.error('회원가입 실패:', err);
      setError(err.response?.data?.message || '회원가입에 실패했습니다.');
      throw err;
    }
  };

  /**
   * 로그아웃 함수
   * 
   * @returns {Promise} 로그아웃 결과
   */
  const logout = () => {
    authAPI.logout();
    localStorage.removeItem('userData'); // 저장된 사용자 정보도 삭제
    setCurrentUser(null);
  };

  // 컨텍스트 값 정의
  const value = {
    currentUser,
    loading,
    error,
    login,
    register,
    logout
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * 인증 컨텍스트 사용 훅
 * 
 * @returns {Object} 인증 컨텍스트 값
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth는 AuthProvider 내에서만 사용할 수 있습니다.');
  }
  return context;
};

export default AuthContext; 