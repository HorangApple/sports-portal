/**
 * 로그인 페이지 컴포넌트
 * 
 * 사용자 로그인 양식과 관련 기능을 제공합니다.
 * 이메일과 비밀번호 입력, 로그인 상태 유지, 소셜 로그인 옵션을 포함합니다.
 */

import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

/**
 * 로그인 페이지 컴포넌트.
 */
const Login = () => {
  // 라우터 훅
  const navigate = useNavigate();
  const location = useLocation();
  
  // 인증 컨텍스트 사용
  const { login, isAuthenticated, loading, error: authError } = useAuth();
  
  // 로컬 상태 관리
  const [formData, setFormData] = useState({
    email: 'test@example.com',
    password: 'password',
    rememberMe: false
  });
  const [error, setError] = useState('');
  
  // 위치 state에서 메시지 가져오기 (예: 회원가입 후 리디렉션)
  const message = location.state?.message || '';
  
  // 이미 인증된 사용자는 홈으로 리디렉션
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);
  
  // 인증 컨텍스트 오류 상태 관찰
  useEffect(() => {
    if (authError) {
      setError(authError);
    }
  }, [authError]);

  /**
   * 입력 필드 변경 핸들러
   * 
   * @param {React.ChangeEvent<HTMLInputElement>} e - 이벤트 객체
   */
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  /**
   * 로그인 폼 제출 핸들러
   * 
   * @param {React.FormEvent<HTMLFormElement>} e - 이벤트 객체
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    try {
      console.log('로그인 시도:', formData.email);
      // Auth 컨텍스트의 login 함수 호출
      const response = await login(formData.email, formData.password, formData.rememberMe);
      console.log('로그인 응답:', response);
      
      // localStorage에 토큰 저장 확인 및 추가 저장
      const token = localStorage.getItem('token');
      console.log('현재 토큰 확인:', token);
      
      if (response && (response.token || response.accessToken)) {
        const tokenValue = response.token || response.accessToken;
        localStorage.setItem('token', tokenValue);
        console.log('토큰 직접 저장:', tokenValue);
      }
      
      // 리디렉션 경로 결정 (예: 이전에 접근하려던 보호된 페이지)
      const redirectPath = location.state?.from || '/';
      navigate(redirectPath);
    } catch (err) {
      console.error('로그인 오류:', err);
      setError(err.message || '로그인 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        {/* 로고 및 헤더 */}
        <div className="text-center">
          <Link to="/" className="inline-block">
            <div className="flex items-center justify-center">
              <div className="w-12 h-12 flex items-center justify-center mr-2 text-primary">
                <i className="ri-award-line text-4xl"></i>
              </div>
              <h2 className="text-3xl font-bold text-gray-900">대한체육회 교육플랫폼</h2>
            </div>
          </Link>
          <h2 className="mt-6 text-2xl font-bold text-gray-900">로그인</h2>
          <p className="mt-2 text-sm text-gray-600">
            계정이 없으신가요?{' '}
            <Link to="/register" className="font-medium text-primary hover:text-primary-dark">
              회원가입
            </Link>
          </p>
        </div>
        
        {/* 회원가입 성공 메시지 */}
        {message && (
          <div className="bg-green-50 border border-green-200 text-green-600 px-4 py-3 rounded-md">
            {message}
          </div>
        )}
        
        {/* 로그인 폼 */}
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          {/* 오류 메시지 */}
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md">
              {error}
            </div>
          )}
          
          <div className="rounded-md shadow-sm space-y-4">
            {/* 이메일 입력 필드 */}
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                이메일
              </label>
              <input
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                required
                value={formData.email}
                onChange={handleChange}
                className="mt-1 relative block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary"
                placeholder="이메일 주소"
              />
            </div>
            
            {/* 비밀번호 입력 필드 */}
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                비밀번호
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="current-password"
                required
                value={formData.password}
                onChange={handleChange}
                className="mt-1 relative block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary"
                placeholder="비밀번호"
              />
            </div>
          </div>

          {/* 로그인 상태 유지 및 비밀번호 찾기 */}
          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <input
                id="rememberMe"
                name="rememberMe"
                type="checkbox"
                checked={formData.rememberMe}
                onChange={handleChange}
                className="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
              />
              <label htmlFor="rememberMe" className="ml-2 block text-sm text-gray-700">
                로그인 상태 유지
              </label>
            </div>

            <div className="text-sm">
              <Link to="/forgot-password" className="font-medium text-primary hover:text-primary-dark">
                비밀번호를 잊으셨나요?
              </Link>
            </div>
          </div>

          {/* 로그인 버튼 */}
          <div>
            <button
              type="submit"
              className={`group relative w-full flex justify-center py-2 px-4 border border-transparent rounded-md text-white bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary ${loading ? 'opacity-70 cursor-not-allowed' : ''}`}
              disabled={loading}
            >
              {loading ? '로그인 중...' : '로그인'}
            </button>
          </div>
          
          {/* 소셜 로그인 */}
          <div className="mt-6">
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300"></div>
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-gray-50 text-gray-500">또는</span>
              </div>
            </div>

            <div className="mt-6 grid grid-cols-2 gap-3">
              {/* 카카오 로그인 버튼 */}
              <button
                type="button"
                className="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary"
                onClick={() => alert('카카오 로그인은 현재 구현 중입니다.')}
              >
                <div className="w-5 h-5 flex items-center justify-center mr-2">
                  <i className="ri-kakao-talk-fill text-yellow-500"></i>
                </div>
                카카오 로그인
              </button>
              
              {/* 네이버 로그인 버튼 */}
              <button
                type="button"
                className="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary"
                onClick={() => alert('네이버 로그인은 현재 구현 중입니다.')}
              >
                <div className="w-5 h-5 flex items-center justify-center mr-2">
                  <i className="ri-naver-fill text-green-600"></i>
                </div>
                네이버 로그인
              </button>
            </div>
          </div>
        </form>
        
        {/* 푸터 */}
        <div className="text-center mt-8">
          <p className="text-xs text-gray-500">
            © {new Date().getFullYear()} 대한체육회 교육플랫폼. All rights reserved.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login; 