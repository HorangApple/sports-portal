import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { bookmarkAPI } from '../services/api';
import { FiBookmark, FiClock, FiStar, FiUser } from 'react-icons/fi';

// 과정 카드 컴포넌트
const CourseCard = ({ course, onRemoveBookmark }) => {
  const handleRemoveBookmark = (e) => {
    e.preventDefault();
    e.stopPropagation();
    onRemoveBookmark(course.id);
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition duration-300">
      <Link to={`/courses/${course.id}`} className="block">
        <img 
          src={course.thumbnail || '/images/course-default.jpg'} 
          alt={course.title}
          className="w-full h-40 object-cover"
        />
        <div className="p-5">
          <div className="flex justify-between items-start">
            <h3 className="font-bold text-lg mb-2 hover:text-primary transition duration-200">
              {course.title}
            </h3>
            <button 
              onClick={handleRemoveBookmark}
              className="text-primary p-1 -mt-1 -mr-1"
              title="북마크 삭제"
            >
              <FiBookmark className="fill-current" />
            </button>
          </div>
          
          <div className="flex items-center mb-2">
            <FiUser className="mr-1 text-gray-500" />
            <p className="text-gray-600 text-sm">{course.instructor || '강사 정보 없음'}</p>
          </div>
          
          <div className="flex items-center mb-2">
            <div className="flex text-yellow-400 mr-1">
              <FiStar className={`${course.rating >= 1 ? "fill-current" : ""}`} />
              <FiStar className={`${course.rating >= 2 ? "fill-current" : ""}`} />
              <FiStar className={`${course.rating >= 3 ? "fill-current" : ""}`} />
              <FiStar className={`${course.rating >= 4 ? "fill-current" : ""}`} />
              <FiStar className={`${course.rating >= 5 ? "fill-current" : ""}`} />
            </div>
            <span className="text-sm text-gray-600">
              ({course.reviewCount || 0})
            </span>
          </div>
          
          <div className="flex items-center justify-between text-sm text-gray-500">
            <div className="flex items-center">
              <FiClock className="mr-1" />
              <span>{course.duration || '기간 정보 없음'}</span>
            </div>
            <span className="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded">
              {course.level || '레벨 정보 없음'}
            </span>
          </div>
        </div>
      </Link>
    </div>
  );
};

// 북마크 페이지 컴포넌트
const Bookmarks = () => {
  const [bookmarkedCourses, setBookmarkedCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchBookmarkedCourses = async () => {
    try {
      setLoading(true);
      const data = await bookmarkAPI.getBookmarkedCourses();
      setBookmarkedCourses(data);
      setError(null);
    } catch (err) {
      console.error('북마크 과정 로딩 실패:', err);
      setError('북마크된 과정을 불러오는 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookmarkedCourses();
  }, []);

  const handleRemoveBookmark = async (courseId) => {
    try {
      await bookmarkAPI.removeBookmark(courseId);
      // 삭제 후 목록에서도 제거
      setBookmarkedCourses(bookmarkedCourses.filter(course => course.id !== courseId));
    } catch (err) {
      console.error(`북마크 삭제 실패 (과정 ID: ${courseId}):`, err);
      // 오류 메시지 표시
      setError('북마크 삭제 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="bg-gray-50 min-h-screen py-8">
      <div className="container mx-auto px-4">
        <h1 className="text-3xl font-bold mb-8">북마크한 강좌</h1>

        {loading ? (
          // 로딩 중 스켈레톤 UI
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="bg-white rounded-lg shadow-sm overflow-hidden animate-pulse">
                <div className="w-full h-40 bg-gray-200"></div>
                <div className="p-4">
                  <div className="h-5 bg-gray-200 rounded mb-2"></div>
                  <div className="h-4 bg-gray-200 rounded w-2/3 mb-2"></div>
                  <div className="h-4 bg-gray-200 rounded w-1/3 mb-3"></div>
                  <div className="flex items-center justify-between">
                    <div className="h-5 bg-gray-200 rounded w-1/4"></div>
                    <div className="h-5 bg-gray-200 rounded w-1/5"></div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : error ? (
          // 오류 발생 시 표시
          <div className="bg-red-50 border border-red-200 text-red-700 rounded-lg p-4">
            <p>{error}</p>
            <button 
              onClick={fetchBookmarkedCourses} 
              className="text-red-700 underline mt-2"
            >
              다시 시도
            </button>
          </div>
        ) : bookmarkedCourses.length === 0 ? (
          // 북마크한 과정이 없는 경우
          <div className="bg-white rounded-lg shadow-sm p-8 text-center">
            <div className="w-16 h-16 rounded-full bg-blue-100 flex items-center justify-center mx-auto mb-4">
              <FiBookmark className="text-primary text-2xl" />
            </div>
            <h3 className="text-xl font-semibold text-gray-700 mb-2">북마크한 강좌가 없습니다</h3>
            <p className="text-gray-500 mb-4">관심 있는 강좌를 북마크에 추가해보세요.</p>
            <Link to="/courses" className="bg-primary text-white px-6 py-2 rounded-button font-medium inline-block">
              강좌 찾아보기
            </Link>
          </div>
        ) : (
          // 북마크한 과정 목록 표시
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {bookmarkedCourses.map(course => (
              <CourseCard 
                key={course.id} 
                course={course} 
                onRemoveBookmark={handleRemoveBookmark} 
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Bookmarks; 