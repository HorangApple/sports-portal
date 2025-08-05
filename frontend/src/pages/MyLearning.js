/**
 * 내 학습 페이지 컴포넌트
 * 
 * 사용자가 수강 중인 강좌 목록과 진행 상황을 표시합니다.
 * 최근 학습한 강좌, 완료한 강좌, 수료증 등을 확인할 수 있습니다.
 */

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';
import { FiClock, FiCalendar, FiAward, FiPlayCircle, FiBarChart2, FiBookmark, FiAlertCircle } from 'react-icons/fi';
import { learningAPI } from '../services/api';
import { format } from 'date-fns';
import placeholderImage from '../assets/images/course-placeholder.jpg';

const MyLearning = () => {
  const [activeTab, setActiveTab] = useState('inProgress');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [courses, setCourses] = useState({
    inProgress: [],
    completed: [],
    saved: []
  });
  const [stats, setStats] = useState({
    totalCourses: 0,
    totalHoursLearned: 0,
    certificatesEarned: 0,
    streakDays: 0
  });

  // 백엔드 응답 데이터를 프론트엔드 표시 형식으로 변환하는 함수
  const transformCourseData = (enrollmentData) => {
    return enrollmentData.map(enrollment => ({
      id: enrollment.id,
      courseId: enrollment.courseId,
      title: enrollment.courseName,
      instructor: "강사 정보", // 백엔드에서 제공하지 않는 정보
      image: placeholderImage, // 백엔드에서 이미지 URL을 제공하지 않으므로 기본 이미지 사용
      duration: "미정", // 실제 데이터에서 산출 필요
      lastAccessed: enrollment.updatedAt ? format(new Date(enrollment.updatedAt), 'yyyy-MM-dd') : '미정',
      completedDate: enrollment.completedAt ? format(new Date(enrollment.completedAt), 'yyyy-MM-dd') : null,
      startDate: enrollment.startDate ? format(new Date(enrollment.startDate), 'yyyy-MM-dd') : '미정',
      endDate: enrollment.endDate ? format(new Date(enrollment.endDate), 'yyyy-MM-dd') : '미정',
      progress: enrollment.completionRate ? Math.round(enrollment.completionRate) : 0,
      completed: 0, // 백엔드에서 제공하지 않는 정보
      total: 1, // 백엔드에서 제공하지 않는 정보
      certificateAvailable: enrollment.completed,
      status: enrollment.status,
      attendanceRate: enrollment.attendanceRate || 0
    }));
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        // 토큰 확인
        const token = localStorage.getItem('token');
        console.log('내 학습 데이터 로드 전 토큰 확인:', token);
        
        if (!token) {
          console.warn('토큰이 없습니다. 로그인이 필요합니다.');
          setError('로그인이 필요합니다. 로그인 후 다시 시도해주세요.');
          setLoading(false);
          return;
        }
        
        // 진행 중인 코스 데이터 가져오기
        console.log('진행 중인 코스 데이터 요청 시작');
        const inProgressData = await learningAPI.getInProgressCourses();
        console.log('진행 중인 코스 데이터 수신:', inProgressData);
        const inProgress = transformCourseData(inProgressData);
        
        // 완료한 코스 데이터 가져오기
        console.log('완료한 코스 데이터 요청 시작');
        const completedData = await learningAPI.getCompletedCourses();
        console.log('완료한 코스 데이터 수신:', completedData);
        const completed = transformCourseData(completedData);

        // 저장한 코스 데이터 가져오기
        console.log('저장한 코스 데이터 요청 시작');
        const savedData = await learningAPI.getSavedCourses();
        console.log('저장한 코스 데이터 수신:', savedData);
        const saved = savedData || []; // 아직 BookmarkAPI가 완전히 구현되지 않았을 수 있음

        // 학습 통계 데이터 가져오기
        console.log('학습 통계 데이터 요청 시작');
        const learningStats = await learningAPI.getLearningStats().catch(() => ({
          totalCourses: inProgress.length + completed.length,
          totalHoursLearned: 0,
          certificatesEarned: completed.length,
          streakDays: 0
        }));
        console.log('학습 통계 데이터 수신:', learningStats);

        setCourses({ inProgress, completed, saved });
        setStats(learningStats);
      } catch (err) {
        console.error('데이터 로딩 실패:', err);
        console.error('에러 정보:', err.response?.data);
        
        if (err.response?.status === 403) {
          setError('접근 권한이 없습니다. 로그인 상태를 확인해주세요.');
        } else {
          setError('학습 데이터를 불러오는 중 오류가 발생했습니다.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleRemoveSaved = async (courseId) => {
    try {
      // 저장 취소 API 호출
      await learningAPI.toggleSaveCourse(courseId, false);

      // 상태 업데이트
      setCourses(prevState => ({
        ...prevState,
        saved: prevState.saved.filter(course => course.id !== courseId)
      }));
    } catch (err) {
      console.error('코스 저장 취소 실패:', err);
      alert('코스 저장 취소 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  const handleCancelEnrollment = async (enrollmentId) => {
    try {
      if (window.confirm('수강 신청을 취소하시겠습니까?')) {
        // 수강 취소 API 호출
        await learningAPI.cancelEnrollment(enrollmentId);

        // 상태 업데이트
        setCourses(prevState => ({
          ...prevState,
          inProgress: prevState.inProgress.filter(course => course.id !== enrollmentId)
        }));
      }
    } catch (err) {
      console.error('수강 취소 실패:', err);
      alert('수강 취소 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  if (loading) {
    return (
      <MainLayout>
        <div className="container mx-auto px-4 py-8">
          <div className="animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/4 mb-8"></div>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
              {[...Array(4)].map((_, i) => (
                <div key={i} className="h-32 bg-gray-200 rounded"></div>
              ))}
            </div>
            <div className="h-8 bg-gray-200 rounded w-1/3 mb-4"></div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {[...Array(3)].map((_, i) => (
                <div key={i} className="h-64 bg-gray-200 rounded"></div>
              ))}
            </div>
          </div>
        </div>
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout>
        <div className="container mx-auto px-4 py-8">
          <div className="text-center py-16">
            <FiAlertCircle className="mx-auto text-red-500 text-4xl mb-4" />
            <h2 className="text-2xl font-bold text-red-600 mb-4">오류 발생</h2>
            <p className="text-gray-600 mb-8">{error}</p>
            <button
              onClick={() => window.location.reload()}
              className="bg-primary hover:bg-primary-dark text-white font-medium py-2 px-6 rounded-md transition duration-200"
            >
              다시 시도
            </button>
          </div>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="bg-gray-50 min-h-screen pb-12">
        <div className="bg-primary-dark text-white py-12">
          <div className="container mx-auto px-4">
            <h1 className="text-3xl font-bold mb-6">내 학습</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <div className="bg-white bg-opacity-10 rounded-lg p-6">
                <div className="flex items-center mb-2">
                  <FiBarChart2 className="mr-2 text-xl" />
                  <h3 className="font-medium">총 수강 강의</h3>
                </div>
                <p className="text-3xl font-bold">{stats.totalCourses}개</p>
              </div>
              <div className="bg-white bg-opacity-10 rounded-lg p-6">
                <div className="flex items-center mb-2">
                  <FiClock className="mr-2 text-xl" />
                  <h3 className="font-medium">총 학습 시간</h3>
                </div>
                <p className="text-3xl font-bold">{stats.totalHoursLearned}시간</p>
              </div>
              <div className="bg-white bg-opacity-10 rounded-lg p-6">
                <div className="flex items-center mb-2">
                  <FiAward className="mr-2 text-xl" />
                  <h3 className="font-medium">획득 수료증</h3>
                </div>
                <p className="text-3xl font-bold">{stats.certificatesEarned}개</p>
              </div>
              <div className="bg-white bg-opacity-10 rounded-lg p-6">
                <div className="flex items-center mb-2">
                  <FiCalendar className="mr-2 text-xl" />
                  <h3 className="font-medium">연속 학습</h3>
                </div>
                <p className="text-3xl font-bold">{stats.streakDays}일</p>
              </div>
            </div>
          </div>
        </div>

        <div className="container mx-auto px-4 py-8">
          {/* 탭 메뉴 */}
          <div className="flex border-b border-gray-200 mb-6">
            <button
              onClick={() => setActiveTab('inProgress')}
              className={`py-3 px-6 font-medium ${
                activeTab === 'inProgress'
                  ? 'border-b-2 border-primary text-primary'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              학습 중 ({courses.inProgress.length})
            </button>
            <button
              onClick={() => setActiveTab('completed')}
              className={`py-3 px-6 font-medium ${
                activeTab === 'completed'
                  ? 'border-b-2 border-primary text-primary'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              완료 ({courses.completed.length})
            </button>
            <button
              onClick={() => setActiveTab('saved')}
              className={`py-3 px-6 font-medium ${
                activeTab === 'saved'
                  ? 'border-b-2 border-primary text-primary'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              저장됨 ({courses.saved.length})
            </button>
          </div>

          {/* 학습 중인 강의 */}
          {activeTab === 'inProgress' && (
            <div>
              <h2 className="text-2xl font-bold mb-6">학습 중인 강의</h2>
              {courses.inProgress.length === 0 ? (
                <div className="text-center py-12 bg-white rounded-lg shadow">
                  <FiPlayCircle className="mx-auto text-gray-400 text-4xl mb-4" />
                  <h3 className="text-xl font-semibold text-gray-700 mb-2">학습 중인 강의가 없습니다</h3>
                  <p className="text-gray-500 mb-6">새로운 강의를 둘러보고 학습을 시작해보세요!</p>
                  <Link to="/courses" className="bg-primary hover:bg-primary-dark text-white font-medium py-2 px-6 rounded-md transition duration-200">
                    강의 둘러보기
                  </Link>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {courses.inProgress.map(course => (
                    <div key={course.id} className="bg-white rounded-lg shadow-md overflow-hidden">
                      <img
                        src={course.image}
                        alt={course.title}
                        className="w-full h-40 object-cover"
                      />
                      <div className="p-5">
                        <h3 className="font-bold text-lg mb-2 hover:text-primary transition duration-200">
                          <Link to={`/course/${course.courseId}`}>
                            {course.title}
                          </Link>
                        </h3>
                        <p className="text-gray-600 text-sm mb-4">{course.instructor}</p>
                        
                        <div className="flex items-center justify-between text-sm text-gray-500 mb-4">
                          <span className="flex items-center">
                            <FiClock className="mr-1" /> {course.duration}
                          </span>
                          <span className="flex items-center">
                            <FiCalendar className="mr-1" /> 최근: {course.lastAccessed}
                          </span>
                        </div>
                        
                        <div className="mb-2">
                          <div className="flex justify-between text-sm mb-1">
                            <span>출석률: {course.attendanceRate || 0}%</span>
                            <span>진도: {course.progress}%</span>
                          </div>
                          <div className="w-full bg-gray-200 rounded-full h-2">
                            <div
                              className="bg-primary h-2 rounded-full"
                              style={{ width: `${course.progress}%` }}
                            ></div>
                          </div>
                        </div>
                        
                        <div className="flex gap-2">
                          <Link
                            to={`/course/${course.courseId}`}
                            className="mt-4 flex-1 block text-center bg-primary hover:bg-primary-dark text-white font-medium py-2 px-4 rounded-md transition duration-200"
                          >
                            학습하기
                          </Link>
                          <button
                            onClick={() => handleCancelEnrollment(course.id)}
                            className="mt-4 flex-none block text-center bg-red-500 hover:bg-red-600 text-white font-medium py-2 px-4 rounded-md transition duration-200"
                          >
                            취소
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* 완료된 강의 */}
          {activeTab === 'completed' && (
            <div>
              <h2 className="text-2xl font-bold mb-6">완료한 강의</h2>
              {courses.completed.length === 0 ? (
                <div className="text-center py-12 bg-white rounded-lg shadow">
                  <FiAward className="mx-auto text-gray-400 text-4xl mb-4" />
                  <h3 className="text-xl font-semibold text-gray-700 mb-2">완료한 강의가 없습니다</h3>
                  <p className="text-gray-500 mb-6">강의를 완료하고 수료증을 획득해보세요!</p>
                  <Link to="/courses" className="bg-primary hover:bg-primary-dark text-white font-medium py-2 px-6 rounded-md transition duration-200">
                    강의 둘러보기
                  </Link>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {courses.completed.map(course => (
                    <div key={course.id} className="bg-white rounded-lg shadow-md overflow-hidden">
                      <div className="relative">
                        <img
                          src={course.image}
                          alt={course.title}
                          className="w-full h-40 object-cover"
                        />
                        <div className="absolute top-0 right-0 bg-green-500 text-white text-xs font-bold px-3 py-1">
                          완료됨
                        </div>
                      </div>
                      <div className="p-5">
                        <h3 className="font-bold text-lg mb-2 hover:text-primary transition duration-200">
                          <Link to={`/course/${course.courseId}`}>
                            {course.title}
                          </Link>
                        </h3>
                        <p className="text-gray-600 text-sm mb-4">{course.instructor}</p>
                        
                        <div className="flex items-center justify-between text-sm text-gray-500 mb-4">
                          <span className="flex items-center">
                            <FiClock className="mr-1" /> {course.duration}
                          </span>
                          <span className="flex items-center">
                            <FiCalendar className="mr-1" /> 완료: {course.completedDate}
                          </span>
                        </div>
                        
                        <div className="w-full bg-gray-200 rounded-full h-2 mb-4">
                          <div className="bg-green-500 h-2 rounded-full w-full"></div>
                        </div>
                        
                        {course.certificateAvailable ? (
                          <a
                            href={`/certificate/${course.id}`}
                            className="mt-2 w-full block text-center bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded-md transition duration-200"
                          >
                            <FiAward className="inline mr-2" />
                            수료증 보기
                          </a>
                        ) : (
                          <button
                            disabled
                            className="mt-2 w-full block text-center bg-gray-300 text-gray-500 font-medium py-2 px-4 rounded-md cursor-not-allowed"
                          >
                            수료증 준비 중
                          </button>
                        )}
                        
                        <Link
                          to={`/course/${course.courseId}`}
                          className="mt-2 w-full block text-center border border-primary text-primary hover:bg-primary hover:text-white font-medium py-2 px-4 rounded-md transition duration-200"
                        >
                          복습하기
                        </Link>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* 저장된 강의 */}
          {activeTab === 'saved' && (
            <div>
              <h2 className="text-2xl font-bold mb-6">저장한 강의</h2>
              {courses.saved.length === 0 ? (
                <div className="text-center py-12 bg-white rounded-lg shadow">
                  <FiBookmark className="mx-auto text-gray-400 text-4xl mb-4" />
                  <h3 className="text-xl font-semibold text-gray-700 mb-2">저장한 강의가 없습니다</h3>
                  <p className="text-gray-500 mb-6">관심있는 강의를 저장하고 나중에 수강해보세요!</p>
                  <Link to="/courses" className="bg-primary hover:bg-primary-dark text-white font-medium py-2 px-6 rounded-md transition duration-200">
                    강의 둘러보기
                  </Link>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {courses.saved.map(course => (
                    <div key={course.id} className="bg-white rounded-lg shadow-md overflow-hidden">
                      <img
                        src={course.image}
                        alt={course.title}
                        className="w-full h-40 object-cover"
                      />
                      <div className="p-5">
                        <h3 className="font-bold text-lg mb-2 hover:text-primary transition duration-200">
                          <Link to={`/course/${course.id}`}>
                            {course.title}
                          </Link>
                        </h3>
                        <p className="text-gray-600 text-sm mb-4">{course.instructor}</p>
                        
                        <div className="flex items-center justify-between text-sm text-gray-500 mb-4">
                          <span>{course.level}</span>
                          <span className="flex items-center">
                            <FiClock className="mr-1" /> {course.duration}
                          </span>
                        </div>
                        
                        <div className="flex mt-4 gap-2">
                          <Link
                            to={`/course/${course.id}`}
                            className="flex-1 block text-center bg-primary hover:bg-primary-dark text-white font-medium py-2 px-4 rounded-md transition duration-200"
                          >
                            자세히 보기
                          </Link>
                          <button
                            onClick={() => handleRemoveSaved(course.id)}
                            className="flex-none block text-center bg-gray-200 hover:bg-gray-300 text-gray-600 font-medium py-2 px-4 rounded-md transition duration-200"
                          >
                            삭제
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </MainLayout>
  );
}

export default MyLearning; 