import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Sidebar = ({ isOpen, closeSidebar }) => {
  const location = useLocation();
  
  // 현재 경로에 따라 active 상태 결정
  const isActive = (path) => {
    return location.pathname === path;
  };

  // 메뉴 항목 정의
  const menuItems = [
    { name: '대시보드', path: '/', icon: 'ri-dashboard-line' },
    { name: '강좌 찾기', path: '/courses', icon: 'ri-book-open-line' },
    { name: '내 학습', path: '/my-learning', icon: 'ri-user-line' },
    { name: '추천 과정', path: '/recommended', icon: 'ri-thumb-up-line' },
    { name: '북마크', path: '/bookmarks', icon: 'ri-bookmark-line' },
    { name: '학습 캘린더', path: '/calendar', icon: 'ri-calendar-line' },
    { name: '수료증', path: '/certificates', icon: 'ri-award-line' },
    { name: '고객센터', path: '/support', icon: 'ri-customer-service-line' },
  ];

  return (
    <>
      {/* 모바일 오버레이 */}
      {isOpen && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
          onClick={closeSidebar}
        ></div>
      )}
      
      {/* 사이드바 */}
      <aside 
        className={`fixed inset-y-0 left-0 transform ${isOpen ? 'translate-x-0' : '-translate-x-full'} md:relative md:translate-x-0 w-64 bg-white border-r border-gray-200 shadow-sm z-30 transition-transform duration-300 ease-in-out`}
      >
        <div className="p-4 border-b border-gray-200">
          <Link to="/" className="font-bold text-2xl text-primary flex items-center">
            <div className="w-10 h-10 flex items-center justify-center mr-2">
              <i className="ri-award-line"></i>
            </div>
            <span>교육플랫폼</span>
          </Link>
        </div>
        
        <div className="flex-1 overflow-y-auto py-4">
          <nav className="px-2 space-y-1">
            {menuItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={`flex items-center px-4 py-3 text-sm font-medium rounded-md ${
                  isActive(item.path)
                    ? 'text-primary bg-blue-50'
                    : 'text-gray-700 hover:bg-gray-50'
                }`}
              >
                <div className={`w-6 h-6 flex items-center justify-center mr-3 ${
                  isActive(item.path) ? 'text-primary' : 'text-gray-500'
                }`}>
                  <i className={item.icon}></i>
                </div>
                {item.name}
              </Link>
            ))}
          </nav>
        </div>
        
        {/* 학습 진행률 */}
        <div className="p-4 border-t border-gray-200">
          <div className="bg-blue-50 p-4 rounded-lg">
            <h3 className="text-sm font-medium text-primary mb-2">학습 진행률</h3>
            <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
              <div className="h-full bg-primary rounded-full" style={{ width: '65%' }}></div>
            </div>
            <p className="text-xs text-gray-500 mt-2">전체 과정의 65% 완료</p>
          </div>
        </div>
      </aside>
    </>
  );
};

export default Sidebar; 