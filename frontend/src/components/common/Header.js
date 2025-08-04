import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const Header = ({ toggleSidebar }) => {
  const navigate = useNavigate();
  // 하드코딩된 상태 대신 AuthContext에서 로그인 상태 가져오기
  const { currentUser, logout } = useAuth();
  const isLoggedIn = !!currentUser;

  const handleLogin = () => {
    navigate('/login');
  };

  const handleRegister = () => {
    navigate('/register');
  };
  
  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="bg-white shadow-sm">
      <div className="container mx-auto px-4 py-3 flex items-center justify-between">
        <div className="flex items-center">
          <button 
            className="md:hidden text-gray-600 hover:text-primary mr-3"
            onClick={toggleSidebar}
            aria-label="메뉴"
          >
            <div className="w-8 h-8 flex items-center justify-center">
              <i className="ri-menu-line"></i>
            </div>
          </button>
          
          <Link to="/" className="text-2xl font-bold text-primary flex items-center">
            <div className="w-10 h-10 flex items-center justify-center mr-2">
              <i className="ri-award-line ri-2x"></i>
            </div>
            <span>대한체육회 교육플랫폼</span>
          </Link>
        </div>
        
        {/* 데스크톱 네비게이션 */}
        <nav className="hidden md:flex items-center space-x-6">
          <Link to="/courses" className="text-gray-700 hover:text-primary font-medium">
            강좌 찾기
          </Link>
          <Link to="/my-learning" className="text-gray-700 hover:text-primary font-medium">
            내 학습
          </Link>
          <Link to="/community" className="text-gray-700 hover:text-primary font-medium">
            커뮤니티
          </Link>
          <Link to="/support" className="text-gray-700 hover:text-primary font-medium">
            고객센터
          </Link>
        </nav>
        
        <div className="flex items-center space-x-4">
          {/* 웹 접근성 도구 */}
          <div className="hidden md:flex items-center space-x-2">
            <button className="text-gray-600 hover:text-primary p-1" aria-label="글자 크게">
              <div className="w-8 h-8 flex items-center justify-center">
                <i className="ri-text"></i>
              </div>
            </button>
            <button className="text-gray-600 hover:text-primary p-1" aria-label="고대비 모드">
              <div className="w-8 h-8 flex items-center justify-center">
                <i className="ri-contrast-2-line"></i>
              </div>
            </button>
          </div>
          
          {/* 검색 버튼 */}
          <button className="text-gray-600 hover:text-primary p-1" aria-label="검색">
            <div className="w-8 h-8 flex items-center justify-center">
              <i className="ri-search-line"></i>
            </div>
          </button>
          
          {/* 로그인/회원가입 또는 사용자 메뉴 */}
          {isLoggedIn ? (
            <div className="relative group">
              <button className="flex items-center space-x-2">
                <div className="w-8 h-8 rounded-full bg-gray-200 flex items-center justify-center">
                  <i className="ri-user-line"></i>
                </div>
                <span className="hidden md:block text-sm font-medium">{currentUser.name || '내 계정'}</span>
              </button>
              <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10 hidden group-hover:block">
                <Link to="/profile" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">프로필</Link>
                <Link to="/settings" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">설정</Link>
                <button 
                  onClick={handleLogout} 
                  className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  로그아웃
                </button>
              </div>
            </div>
          ) : (
            <>
              <button onClick={handleLogin} className="hidden md:block text-gray-700 hover:text-primary whitespace-nowrap">
                로그인
              </button>
              <button onClick={handleRegister} className="hidden md:block bg-primary text-white px-4 py-2 rounded-button whitespace-nowrap">
                회원가입
              </button>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header; 