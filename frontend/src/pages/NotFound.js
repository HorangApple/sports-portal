import React from 'react';
import { Link } from 'react-router-dom';

const NotFound = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4 py-12">
      <div className="max-w-md w-full text-center">
        <div className="w-24 h-24 mx-auto mb-6 text-primary">
          <i className="ri-error-warning-line text-8xl"></i>
        </div>
        <h1 className="text-6xl font-bold text-gray-900 mb-4">404</h1>
        <h2 className="text-2xl font-semibold text-gray-800 mb-4">페이지를 찾을 수 없습니다</h2>
        <p className="text-gray-600 mb-8">
          요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.
        </p>
        <div className="flex flex-col sm:flex-row justify-center gap-4">
          <Link
            to="/"
            className="bg-primary text-white px-6 py-3 rounded-md font-medium hover:bg-primary-dark"
          >
            홈으로 이동
          </Link>
          <button
            onClick={() => window.history.back()}
            className="bg-white text-gray-700 border border-gray-300 px-6 py-3 rounded-md font-medium hover:bg-gray-50"
          >
            이전 페이지로 이동
          </button>
        </div>
      </div>
    </div>
  );
};

export default NotFound; 