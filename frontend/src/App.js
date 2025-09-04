/**
 * 애플리케이션 메인 컴포넌트
 * 
 * 전체 라우팅 구조를 정의하고 필수 컨텍스트 프로바이더를 설정합니다.
 * 사용자 인증 상태에 따라 접근 제한이 필요한 라우트는 ProtectedRoute 컴포넌트로 감싸야 합니다.
 */

import React from 'react';
import { Routes, Route } from 'react-router-dom';

// 레이아웃 및 페이지 컴포넌트
import MainLayout from './layouts/MainLayout';
import Dashboard from './pages/Dashboard';
import Courses from './pages/Courses';
import CourseDetail from './pages/CourseDetail';
import MyLearning from './pages/MyLearning';
import Recommended from './pages/Recommended';
import Calendar from './pages/Calendar';
import Login from './pages/Login';
import Register from './pages/Register';
import NotFound from './pages/NotFound';
import Bookmarks from './pages/Bookmarks';

// 컨텍스트 프로바이더
import { AuthProvider } from './contexts/AuthContext';
import { CourseProvider } from './contexts/CourseContext';

/**
 * 인증이 필요한 라우트를 보호하는 컴포넌트
 * 
 * @param {Object} props - 컴포넌트 프롭스
 * @param {React.ReactNode} props.children - 자식 컴포넌트
 */
// 참고: 실제 구현 시에는 인증 상태 확인 로직 추가 필요

/**
 * 애플리케이션 메인 컴포넌트
 */
function App() {
  return (
    // 인증 및 강좌 컨텍스트 제공
    <AuthProvider>
      <CourseProvider>
        <Routes>
          {/* 메인 레이아웃이 적용된 라우트 */}
          <Route path="/" element={<MainLayout />}>
            {/* 대시보드 - 애플리케이션 홈 */}
            <Route index element={<Dashboard />} />

            {/* 나의 학습 페이지 - 인증 필요 */}
            <Route path="my-learning" element={<MyLearning />} />
          </Route>

          {/* 인증 관련 라우트 - 레이아웃 없음 */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* 404 페이지 - 존재하지 않는 모든 경로에 대응 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </CourseProvider>
    </AuthProvider>
  );
}

export default App; 