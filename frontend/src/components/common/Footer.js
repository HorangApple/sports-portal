import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <footer className="bg-white border-t border-gray-200">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="text-lg font-semibold text-gray-800 mb-4">대한체육회 교육플랫폼</h3>
            <p className="text-gray-600 mb-4">
              스포츠 전문가로 성장하는 교육의 시작, 대한체육회 교육플랫폼에서 다양한 교육과정을 만나보세요.
            </p>
            <div className="flex space-x-4">
              <Link to="#" className="text-gray-500 hover:text-primary">
                <span className="w-6 h-6 flex items-center justify-center">
                  <i className="ri-facebook-fill"></i>
                </span>
              </Link>
              <Link to="#" className="text-gray-500 hover:text-primary">
                <span className="w-6 h-6 flex items-center justify-center">
                  <i className="ri-instagram-line"></i>
                </span>
              </Link>
              <Link to="#" className="text-gray-500 hover:text-primary">
                <span className="w-6 h-6 flex items-center justify-center">
                  <i className="ri-youtube-line"></i>
                </span>
              </Link>
            </div>
          </div>
          
          <div>
            <h3 className="text-lg font-semibold text-gray-800 mb-4">바로가기</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/" className="text-gray-600 hover:text-primary">홈</Link>
              </li>
              <li>
                <Link to="/courses" className="text-gray-600 hover:text-primary">강좌 찾기</Link>
              </li>
              <li>
                <Link to="/my-learning" className="text-gray-600 hover:text-primary">내 학습</Link>
              </li>
              <li>
                <Link to="/community" className="text-gray-600 hover:text-primary">커뮤니티</Link>
              </li>
              <li>
                <Link to="/support" className="text-gray-600 hover:text-primary">고객센터</Link>
              </li>
            </ul>
          </div>
          
          <div>
            <h3 className="text-lg font-semibold text-gray-800 mb-4">고객지원</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/faq" className="text-gray-600 hover:text-primary">자주 묻는 질문</Link>
              </li>
              <li>
                <Link to="/inquiry" className="text-gray-600 hover:text-primary">1:1 문의</Link>
              </li>
              <li>
                <Link to="/terms" className="text-gray-600 hover:text-primary">이용약관</Link>
              </li>
              <li>
                <Link to="/privacy" className="text-gray-600 hover:text-primary">개인정보처리방침</Link>
              </li>
            </ul>
          </div>
          
          <div>
            <h3 className="text-lg font-semibold text-gray-800 mb-4">문의하기</h3>
            <div className="text-gray-600 mb-2">
              <span className="flex items-center">
                <span className="w-5 h-5 flex items-center justify-center mr-2 text-primary">
                  <i className="ri-map-pin-line"></i>
                </span>
                서울특별시 송파구 올림픽로 424
              </span>
            </div>
            <div className="text-gray-600 mb-2">
              <span className="flex items-center">
                <span className="w-5 h-5 flex items-center justify-center mr-2 text-primary">
                  <i className="ri-phone-line"></i>
                </span>
                02-1234-5678
              </span>
            </div>
            <div className="text-gray-600 mb-2">
              <span className="flex items-center">
                <span className="w-5 h-5 flex items-center justify-center mr-2 text-primary">
                  <i className="ri-mail-line"></i>
                </span>
                education@sports.or.kr
              </span>
            </div>
          </div>
        </div>
        
        <div className="border-t border-gray-200 mt-8 pt-6">
          <p className="text-sm text-gray-500 text-center">
            © {new Date().getFullYear()} 대한체육회 교육플랫폼. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 