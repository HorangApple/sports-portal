import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { courseAPI, learningAPI } from '../services/api';

// 목업 데이터
const featuredCourses = [
  {
    id: 1,
    title: '스포츠 트레이닝 기초',
    category: '체육 기초',
    instructor: '김태양',
    thumbnail: '/images/course-sports.jpg',
    level: '초급',
    duration: '4주',
    rating: 4.8,
    enrolled: 1245
  },
  {
    id: 2,
    title: '운동 생리학 심화과정',
    category: '체육 이론',
    instructor: '이하늘',
    thumbnail: '/images/course-physiology.jpg',
    level: '중급',
    duration: '8주',
    rating: 4.5,
    enrolled: 758
  },
  {
    id: 3,
    title: '스포츠 심리학 입문',
    category: '체육 심리',
    instructor: '박바다',
    thumbnail: '/images/course-psychology.jpg',
    level: '초급',
    duration: '6주',
    rating: 4.7,
    enrolled: 932
  }
];

const myCourses = [
  {
    id: 101,
    title: '스포츠 지도자 자격과정',
    progress: 65,
    lastAccessed: '2025-04-15',
    dueDate: '2025-05-30',
    thumbnail: '/images/course-coach.jpg'
  },
  {
    id: 102,
    title: '스포츠 영양학 기본',
    progress: 30,
    lastAccessed: '2025-04-10',
    dueDate: '2025-06-15',
    thumbnail: '/images/course-nutrition.jpg'
  }
];

const categories = [
  { id: 1, name: '체육 기초', count: 12, icon: 'ri-run-line' },
  { id: 2, name: '체육 이론', count: 8, icon: 'ri-book-open-line' },
  { id: 3, name: '체육 실기', count: 15, icon: 'ri-football-line' },
  { id: 4, name: '지도자 과정', count: 6, icon: 'ri-user-star-line' },
  { id: 5, name: '심판 과정', count: 4, icon: 'ri-flag-line' },
  { id: 6, name: '스포츠 의학', count: 7, icon: 'ri-heart-pulse-line' }
];

const CourseCard = ({ course }) => {
  return (
    <div className="bg-white rounded-lg shadow-sm overflow-hidden transition-transform hover:shadow-md hover:-translate-y-1">
      <div className="relative">
        <img 
          src={course.thumbnail} 
          alt={course.title} 
          className="w-full h-48 object-cover"
        />
        <div className="absolute top-0 right-0 bg-primary text-white px-2 py-1 text-xs font-medium m-2 rounded">
          {course.level}
        </div>
      </div>
      <div className="p-4">
        <div className="text-xs text-gray-500 mb-1">{course.category}</div>
        <h3 className="text-lg font-semibold mb-2">{course.title}</h3>
        <div className="flex items-center mb-3">
          <div className="w-5 h-5 flex items-center justify-center mr-1 text-primary">
            <i className="ri-user-line"></i>
          </div>
          <span className="text-sm text-gray-600">{course.instructor}</span>
        </div>
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <div className="w-5 h-5 flex items-center justify-center mr-1 text-yellow-500">
              <i className="ri-star-fill"></i>
            </div>
            <span className="text-sm font-medium">{course.rating}</span>
            <span className="mx-1 text-gray-400">|</span>
            <span className="text-sm text-gray-600">{course.enrolled} 수강생</span>
          </div>
          <div className="text-sm text-gray-600">
            {course.duration}
          </div>
        </div>
      </div>
    </div>
  );
};

const MyCourseCard = ({ course }) => {
  return (
    <div className="bg-white rounded-lg shadow-sm p-4 flex flex-col md:flex-row gap-4">
      <div className="w-full md:w-32 h-24 rounded overflow-hidden">
        <img 
          src={course.thumbnail} 
          alt={course.title} 
          className="w-full h-full object-cover"
        />
      </div>
      <div className="flex-1">
        <h3 className="text-lg font-medium mb-2">{course.title}</h3>
        <div className="flex flex-col md:flex-row md:items-center gap-2 md:gap-4 mb-3">
          <div className="flex items-center">
            <div className="w-4 h-4 flex items-center justify-center mr-1 text-gray-500">
              <i className="ri-time-line"></i>
            </div>
            <span className="text-sm text-gray-600">마지막 접속: {course.lastAccessed}</span>
          </div>
          <div className="flex items-center">
            <div className="w-4 h-4 flex items-center justify-center mr-1 text-gray-500">
              <i className="ri-calendar-event-line"></i>
            </div>
            <span className="text-sm text-gray-600">완료 기한: {course.dueDate}</span>
          </div>
        </div>
        <div>
          <div className="flex items-center justify-between mb-1">
            <span className="text-sm font-medium">{course.progress}% 완료</span>
            <Link to={`/my-learning/${course.id}`} className="text-primary text-sm hover:underline">
              이어서 학습하기
            </Link>
          </div>
          <div className="w-full h-2 bg-gray-200 rounded-full overflow-hidden">
            <div 
              className="h-full bg-primary rounded-full"
              style={{ width: `${course.progress}%` }}
            ></div>
          </div>
        </div>
      </div>
    </div>
  );
};

const Dashboard = () => {
  const [featuredCourses, setFeaturedCourses] = useState([]);
  const [myCourses, setMyCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        
        // 추천 과정 데이터 가져오기
        const recommendedCoursesData = await courseAPI.getPopularCourses(3);
        setFeaturedCourses(recommendedCoursesData);
        
        // 내 학습 과정 데이터 가져오기
        try {
          const myCoursesData = await learningAPI.getInProgressCourses();
          setMyCourses(myCoursesData);
        } catch (err) {
          console.error('내 학습 데이터 로딩 실패:', err);
          // 오류 발생 시 목업 데이터 사용
          setMyCourses(myCourses);
        }
        
        setError(null);
      } catch (err) {
        console.error('대시보드 데이터 로딩 실패:', err);
        setError('대시보드 데이터를 불러오는 중 오류가 발생했습니다.');
        
        // 오류 발생 시 더미 데이터 사용
        setFeaturedCourses([
          {
            id: 1,
            title: '스포츠 트레이닝 기초',
            category: '체육 기초',
            instructor: '김태양',
            thumbnail: '/images/course-sports.jpg',
            level: '초급',
            duration: '4주',
            rating: 4.8,
            enrolled: 1245
          },
          {
            id: 2,
            title: '운동 생리학 심화과정',
            category: '체육 이론',
            instructor: '이하늘',
            thumbnail: '/images/course-physiology.jpg',
            level: '중급',
            duration: '8주',
            rating: 4.5,
            enrolled: 758
          },
          {
            id: 3,
            title: '스포츠 심리학 입문',
            category: '체육 심리',
            instructor: '박바다',
            thumbnail: '/images/course-psychology.jpg',
            level: '초급',
            duration: '6주',
            rating: 4.7,
            enrolled: 932
          }
        ]);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  return (
    <div className="container mx-auto px-4">
      {/* 히어로 섹션 */}
      <section className="relative overflow-hidden rounded-xl mb-8" style={{ background: 'linear-gradient(to right, #0066cc, #4D9CFF)' }}>
        <div className="absolute inset-0 flex items-center justify-end">
          <div className="h-full w-1/2 bg-cover bg-center opacity-20" style={{ backgroundImage: `url('/images/hero-sports.jpg')` }}></div>
        </div>
        <div className="relative z-10 py-12 px-6 md:py-16 md:px-10">
          <div className="max-w-2xl">
            <h1 className="text-3xl md:text-4xl font-bold text-white mb-4">
              스포츠 전문가로 성장하는 <br />
              <span className="text-yellow-300">체육 교육의 새로운 시작</span>
            </h1>
            <p className="text-lg text-white/90 mb-8">
              대한체육회가 제공하는 고품질 교육 콘텐츠로 언제 어디서나 <br />
              전문적인 체육 지식과 기술을 습득하세요.
            </p>
            <div className="flex flex-col sm:flex-row gap-4">
              <Link to="/courses" className="bg-white text-primary px-6 py-3 rounded-button font-medium text-center whitespace-nowrap">
                무료 강좌 둘러보기
              </Link>
              <Link to="/about" className="bg-transparent text-white border border-white px-6 py-3 rounded-button font-medium text-center whitespace-nowrap">
                교육 과정 안내
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* 나의 학습 현황 */}
      <section className="mb-10">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-900">나의 학습 현황</h2>
          <Link to="/my-learning" className="text-primary hover:underline flex items-center">
            <span>전체보기</span>
            <div className="w-5 h-5 flex items-center justify-center ml-1">
              <i className="ri-arrow-right-line"></i>
            </div>
          </Link>
        </div>
        
        <div className="grid grid-cols-1 gap-4">
          {myCourses.length > 0 ? (
            myCourses.map(course => (
              <MyCourseCard key={course.id} course={course} />
            ))
          ) : (
            <div className="bg-white rounded-lg shadow-sm p-8 text-center">
              <div className="w-16 h-16 rounded-full bg-blue-100 flex items-center justify-center mx-auto mb-4">
                <i className="ri-book-open-line text-primary text-2xl"></i>
              </div>
              <h3 className="text-lg font-medium text-gray-900 mb-2">아직 수강 중인 강좌가 없습니다</h3>
              <p className="text-gray-600 mb-4">다양한 교육과정을 살펴보고 원하는 강좌를 찾아보세요.</p>
              <Link to="/courses" className="bg-primary text-white px-6 py-2 rounded-button font-medium inline-block">
                강좌 찾기
              </Link>
            </div>
          )}
        </div>
      </section>

      {/* 카테고리 */}
      <section className="mb-10">
        <h2 className="text-2xl font-bold text-gray-900 mb-6">카테고리</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
          {categories.map(category => (
            <Link 
              key={category.id}
              to={`/courses?category=${category.id}`}
              className="bg-white rounded-lg shadow-sm p-4 text-center hover:shadow-md transition-shadow"
            >
              <div className="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center mx-auto mb-3">
                <i className={`${category.icon} text-primary text-xl`}></i>
              </div>
              <h3 className="font-medium text-gray-900 mb-1">{category.name}</h3>
              <p className="text-sm text-gray-500">{category.count}개 과정</p>
            </Link>
          ))}
        </div>
      </section>

      {/* 추천 강좌 */}
      <section className="mb-10">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-900">추천 강좌</h2>
          <Link to="/courses" className="text-primary hover:underline flex items-center">
            <span>더보기</span>
            <div className="w-5 h-5 flex items-center justify-center ml-1">
              <i className="ri-arrow-right-line"></i>
            </div>
          </Link>
        </div>
        
        {loading ? (
          // 로딩 중 스켈레톤 UI
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="bg-white rounded-lg shadow-sm overflow-hidden animate-pulse">
                <div className="w-full h-48 bg-gray-200"></div>
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
              onClick={() => window.location.reload()} 
              className="text-red-700 underline mt-2"
            >
              새로고침
            </button>
          </div>
        ) : (
          // 추천 강좌 표시
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {featuredCourses.map(course => (
              <Link key={course.id} to={`/courses/${course.id}`}>
                <CourseCard course={course} />
              </Link>
            ))}
          </div>
        )}
      </section>

      {/* 공지사항 */}
      <section className="mb-10">
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-900">공지사항</h2>
            <Link to="/notice" className="text-primary hover:underline text-sm">더보기</Link>
          </div>
          
          <div className="space-y-4">
            <div className="border-b border-gray-100 pb-4">
              <h3 className="text-base font-medium hover:text-primary">
                <Link to="/notice/1">2025년 체육 지도자 자격 취득 과정 안내</Link>
              </h3>
              <div className="flex items-center mt-2 text-sm text-gray-500">
                <span>2025.04.01</span>
                <span className="mx-2">|</span>
                <span>관리자</span>
              </div>
            </div>
            <div className="border-b border-gray-100 pb-4">
              <h3 className="text-base font-medium hover:text-primary">
                <Link to="/notice/2">온라인 교육 이수 안내 및 주의사항</Link>
              </h3>
              <div className="flex items-center mt-2 text-sm text-gray-500">
                <span>2025.03.15</span>
                <span className="mx-2">|</span>
                <span>관리자</span>
              </div>
            </div>
            <div>
              <h3 className="text-base font-medium hover:text-primary">
                <Link to="/notice/3">시스템 점검 안내 (4/10 00:00-04:00)</Link>
              </h3>
              <div className="flex items-center mt-2 text-sm text-gray-500">
                <span>2025.04.05</span>
                <span className="mx-2">|</span>
                <span>관리자</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Dashboard; 