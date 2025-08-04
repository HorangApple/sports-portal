/**
 * 인증 컨텍스트 테스트 파일
 * 
 * AuthContext 및 관련 훅의 기능을 테스트합니다.
 */

import React from 'react';
import { render, screen, act, waitFor } from '@testing-library/react';
import { AuthProvider, useAuth } from '../AuthContext';

// 모킹 설정
jest.mock('../../services/api', () => ({
  authAPI: {
    login: jest.fn(),
    register: jest.fn(),
    logout: jest.fn(),
  }
}));

// 테스트용 컴포넌트
const TestComponent = () => {
  const {
    user,
    loading,
    error,
    isAuthenticated,
    login,
    logout,
    register
  } = useAuth();
  
  return (
    <div>
      <div data-testid="loading">{loading.toString()}</div>
      <div data-testid="authenticated">{isAuthenticated.toString()}</div>
      <div data-testid="error">{error || 'no-error'}</div>
      <div data-testid="user">{user ? JSON.stringify(user) : 'no-user'}</div>
      <button onClick={() => login('test@example.com', 'password', false)}>로그인</button>
      <button onClick={() => logout()}>로그아웃</button>
      <button onClick={() => register({ name: 'Test User', email: 'test@example.com', password: 'password' })}>회원가입</button>
    </div>
  );
};

describe('인증 컨텍스트', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });
  
  test('초기 상태가 올바르게 설정되는지 확인', () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );
    
    // 초기 로딩 상태는 true
    // 참고: useEffect가 비동기로 실행되므로 초기에는 loading=true
    expect(screen.getByTestId('loading').textContent).toBe('true');
    
    // 초기 인증 상태는 false
    expect(screen.getByTestId('authenticated').textContent).toBe('false');
    
    // 초기 오류는 없음
    expect(screen.getByTestId('error').textContent).toBe('no-error');
    
    // 초기 사용자 정보는 없음
    expect(screen.getByTestId('user').textContent).toBe('no-user');
  });
  
  test('로그인 함수가 성공적으로 작동하는지 확인', async () => {
    const mockUser = {
      id: 1,
      name: '김체육',
      email: 'test@example.com',
      role: 'user'
    };
    
    // 모의 응답 설정
    const { authAPI } = require('../../services/api');
    authAPI.login.mockResolvedValue({
      user: mockUser,
      token: 'test-token',
      refreshToken: 'test-refresh-token'
    });
    
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );
    
    // 로그인 버튼 클릭
    act(() => {
      screen.getByText('로그인').click();
    });
    
    // 로그인 성공 후 상태 확인
    await waitFor(() => {
      // loading 상태가 false로 변경되었는지 확인
      expect(screen.getByTestId('loading').textContent).toBe('false');
      
      // 인증 상태가 true로 변경되었는지 확인
      expect(screen.getByTestId('authenticated').textContent).toBe('true');
      
      // 사용자 정보가 업데이트되었는지 확인
      expect(screen.getByTestId('user').textContent).toContain('김체육');
      
      // 토큰이 localStorage에 저장되었는지 확인
      expect(localStorage.getItem('token')).toBe('test-token');
      expect(localStorage.getItem('refreshToken')).toBe('test-refresh-token');
    });
  });
  
  test('로그인 실패 시 오류가 올바르게 처리되는지 확인', async () => {
    const errorMessage = '이메일 또는 비밀번호가 올바르지 않습니다.';
    
    // 모의 응답 설정
    const { authAPI } = require('../../services/api');
    authAPI.login.mockRejectedValue(new Error(errorMessage));
    
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );
    
    // 로그인 버튼 클릭
    act(() => {
      screen.getByText('로그인').click();
    });
    
    // 오류 상태 확인
    await waitFor(() => {
      expect(screen.getByTestId('error').textContent).toBe(errorMessage);
      expect(screen.getByTestId('authenticated').textContent).toBe('false');
    });
  });
  
  test('로그아웃 함수가 올바르게 작동하는지 확인', async () => {
    // 먼저 로그인 상태 설정
    localStorage.setItem('token', 'test-token');
    localStorage.setItem('refreshToken', 'test-refresh-token');
    
    // 모의 응답 설정
    const { authAPI } = require('../../services/api');
    authAPI.logout.mockResolvedValue({ success: true });
    
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );
    
    // 로그아웃 버튼 클릭
    act(() => {
      screen.getByText('로그아웃').click();
    });
    
    // 로그아웃 후 상태 확인
    await waitFor(() => {
      // 인증 상태가 false로 변경되었는지 확인
      expect(screen.getByTestId('authenticated').textContent).toBe('false');
      
      // 사용자 정보가 초기화되었는지 확인
      expect(screen.getByTestId('user').textContent).toBe('no-user');
      
      // localStorage에서 토큰이 삭제되었는지 확인
      expect(localStorage.getItem('token')).toBeNull();
      expect(localStorage.getItem('refreshToken')).toBeNull();
    });
  });
}); 