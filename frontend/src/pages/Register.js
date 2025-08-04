/**
 * 회원가입 페이지 컴포넌트
 * 
 * 사용자 등록 양식과 관련 기능을 제공합니다.
 * 개인 정보, 비밀번호 설정, 이용약관 동의 등의 기능을 포함합니다.
 */

import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

/**
 * 회원가입 페이지 컴포넌트
 */
const Register = () => {
  // 라우터 훅
  const navigate = useNavigate();
  
  // 인증 컨텍스트 사용
  const { register, isAuthenticated, loading, error: authError } = useAuth();
  
  // 로컬 상태 관리
  const [formData, setFormData] = useState({
    name: '',
    email: 'test2@example.com',
    password: 'password',
    confirmPassword: 'password',
    phoneNumber: '',
    agreeTerms: false,
    agreePrivacy: false,
    agreeMarketing: false
  });
  const [error, setError] = useState('');
  const [errors, setErrors] = useState({});
  
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
    
    // 실시간 유효성 검사
    if (name === 'confirmPassword' && formData.password !== value) {
      setErrors({ ...errors, confirmPassword: '비밀번호가 일치하지 않습니다.' });
    } else if (name === 'confirmPassword') {
      setErrors({ ...errors, confirmPassword: '' });
    }
  };

  /**
   * 폼 유효성 검사 함수
   * 
   * @returns {boolean} 유효성 검사 통과 여부
   */
  const validate = () => {
    const newErrors = {};
    
    // 이름 검사
    if (!formData.name.trim()) {
      newErrors.name = '이름을 입력해주세요.';
    }
    
    // 이메일 검사
    if (!formData.email.trim()) {
      newErrors.email = '이메일을 입력해주세요.';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = '유효한 이메일 주소를 입력해주세요.';
    }
    
    // 비밀번호 검사
    if (!formData.password) {
      newErrors.password = '비밀번호를 입력해주세요.';
    } else if (formData.password.length < 8) {
      newErrors.password = '비밀번호는 8자 이상이어야 합니다.';
    }
    
    // 비밀번호 확인 검사
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = '비밀번호가 일치하지 않습니다.';
    }
    
    // 전화번호 검사
    if (!formData.phoneNumber.trim()) {
      newErrors.phoneNumber = '전화번호를 입력해주세요.';
    } else if (!/^\d{3}-\d{3,4}-\d{4}$/.test(formData.phoneNumber)) {
      newErrors.phoneNumber = '올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)';
    }
    
    // 약관 동의 검사
    if (!formData.agreeTerms) {
      newErrors.agreeTerms = '이용약관에 동의해주세요.';
    }
    
    if (!formData.agreePrivacy) {
      newErrors.agreePrivacy = '개인정보 처리방침에 동의해주세요.';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  /**
   * 회원가입 폼 제출 핸들러
   * 
   * @param {React.FormEvent<HTMLFormElement>} e - 이벤트 객체
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // 유효성 검사
    if (!validate()) {
      return;
    }
    
    setError('');
    
    try {
      // Auth 컨텍스트의 register 함수 호출
      const userData = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
        phoneNumber: formData.phoneNumber,
        marketingConsent: formData.agreeMarketing
      };
      
      await register(userData);
      
      // 성공 메시지와 함께 로그인 페이지로 리디렉션
      navigate('/login', { 
        state: { message: '회원가입이 완료되었습니다. 로그인해주세요.' } 
      });
    } catch (err) {
      setError(err.message || '회원가입 중 오류가 발생했습니다.');
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
          <h2 className="mt-6 text-2xl font-bold text-gray-900">회원가입</h2>
          <p className="mt-2 text-sm text-gray-600">
            이미 계정이 있으신가요?{' '}
            <Link to="/login" className="font-medium text-primary hover:text-primary-dark">
              로그인
            </Link>
          </p>
        </div>
        
        {/* 회원가입 폼 */}
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          {/* 오류 메시지 */}
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md">
              {error}
            </div>
          )}
          
          <div className="rounded-md shadow-sm space-y-4">
            {/* 이름 입력 필드 */}
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                이름 <span className="text-red-500">*</span>
              </label>
              <input
                id="name"
                name="name"
                type="text"
                autoComplete="name"
                required
                value={formData.name}
                onChange={handleChange}
                className={`mt-1 relative block w-full px-3 py-2 border ${errors.name ? 'border-red-300' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-primary focus:border-primary`}
                placeholder="이름"
              />
              {errors.name && <p className="mt-1 text-sm text-red-600">{errors.name}</p>}
            </div>
            
            {/* 이메일 입력 필드 */}
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                이메일 <span className="text-red-500">*</span>
              </label>
              <input
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                required
                value={formData.email}
                onChange={handleChange}
                className={`mt-1 relative block w-full px-3 py-2 border ${errors.email ? 'border-red-300' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-primary focus:border-primary`}
                placeholder="이메일 주소"
              />
              {errors.email && <p className="mt-1 text-sm text-red-600">{errors.email}</p>}
            </div>
            
            {/* 비밀번호 입력 필드 */}
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                비밀번호 <span className="text-red-500">*</span>
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="new-password"
                required
                value={formData.password}
                onChange={handleChange}
                className={`mt-1 relative block w-full px-3 py-2 border ${errors.password ? 'border-red-300' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-primary focus:border-primary`}
                placeholder="비밀번호 (8자 이상)"
              />
              {errors.password && <p className="mt-1 text-sm text-red-600">{errors.password}</p>}
            </div>
            
            {/* 비밀번호 확인 입력 필드 */}
            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">
                비밀번호 확인 <span className="text-red-500">*</span>
              </label>
              <input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                autoComplete="new-password"
                required
                value={formData.confirmPassword}
                onChange={handleChange}
                className={`mt-1 relative block w-full px-3 py-2 border ${errors.confirmPassword ? 'border-red-300' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-primary focus:border-primary`}
                placeholder="비밀번호 확인"
              />
              {errors.confirmPassword && <p className="mt-1 text-sm text-red-600">{errors.confirmPassword}</p>}
            </div>
            
            {/* 전화번호 입력 필드 */}
            <div>
              <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                전화번호 <span className="text-red-500">*</span>
              </label>
              <input
                id="phoneNumber"
                name="phoneNumber"
                type="tel"
                autoComplete="tel"
                required
                value={formData.phoneNumber}
                onChange={handleChange}
                className={`mt-1 relative block w-full px-3 py-2 border ${errors.phoneNumber ? 'border-red-300' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-primary focus:border-primary`}
                placeholder="010-1234-5678"
              />
              {errors.phoneNumber && <p className="mt-1 text-sm text-red-600">{errors.phoneNumber}</p>}
            </div>
          </div>

          {/* 약관 동의 체크박스 */}
          <div className="space-y-4">
            {/* 이용약관 동의 */}
            <div className="flex items-start">
              <div className="flex items-center h-5">
                <input
                  id="agreeTerms"
                  name="agreeTerms"
                  type="checkbox"
                  checked={formData.agreeTerms}
                  onChange={handleChange}
                  required
                  className="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
                />
              </div>
              <div className="ml-3 text-sm">
                <label htmlFor="agreeTerms" className="font-medium text-gray-700">
                  이용약관 동의 <span className="text-red-500">*</span>
                </label>
                <p className="text-gray-500">대한체육회 교육플랫폼 이용약관에 동의합니다.</p>
                {errors.agreeTerms && <p className="mt-1 text-sm text-red-600">{errors.agreeTerms}</p>}
              </div>
            </div>
            
            {/* 개인정보 처리방침 동의 */}
            <div className="flex items-start">
              <div className="flex items-center h-5">
                <input
                  id="agreePrivacy"
                  name="agreePrivacy"
                  type="checkbox"
                  checked={formData.agreePrivacy}
                  onChange={handleChange}
                  required
                  className="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
                />
              </div>
              <div className="ml-3 text-sm">
                <label htmlFor="agreePrivacy" className="font-medium text-gray-700">
                  개인정보 처리방침 동의 <span className="text-red-500">*</span>
                </label>
                <p className="text-gray-500">개인정보 수집 및 이용에 대한 안내에 동의합니다.</p>
                {errors.agreePrivacy && <p className="mt-1 text-sm text-red-600">{errors.agreePrivacy}</p>}
              </div>
            </div>
            
            {/* 마케팅 정보 수신 동의 */}
            <div className="flex items-start">
              <div className="flex items-center h-5">
                <input
                  id="agreeMarketing"
                  name="agreeMarketing"
                  type="checkbox"
                  checked={formData.agreeMarketing}
                  onChange={handleChange}
                  className="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
                />
              </div>
              <div className="ml-3 text-sm">
                <label htmlFor="agreeMarketing" className="font-medium text-gray-700">
                  마케팅 정보 수신 동의 <span className="text-gray-500">(선택)</span>
                </label>
                <p className="text-gray-500">이벤트 및 마케팅 정보 수신에 동의합니다.</p>
              </div>
            </div>
          </div>

          {/* 회원가입 버튼 */}
          <div>
            <button
              type="submit"
              className={`group relative w-full flex justify-center py-2 px-4 border border-transparent rounded-md text-white bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary ${loading ? 'opacity-70 cursor-not-allowed' : ''}`}
              disabled={loading}
            >
              {loading ? '처리 중...' : '회원가입'}
            </button>
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

export default Register; 