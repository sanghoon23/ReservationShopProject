import { format, startOfMonth, endOfMonth, getDay, getDaysInMonth, isToday, subMonths, addMonths, isBefore, startOfDay } from 'https://cdn.jsdelivr.net/npm/date-fns@2.29.3/+esm';

// 시간 슬롯 컨테이너를 업데이트하는 함수
function updateAvailableTimes(date) {
    const timeSlotsContainer = document.getElementById('timeSlotsContainer');

    if (!timeSlotsContainer) {
        console.error("시간 슬롯 컨테이너를 찾을 수 없습니다.");
        return;
    }

    // 현재 시간 구하기
    const now = new Date();

    timeSlotsContainer.innerHTML = ''; // 기존 시간 슬롯 내용 제거

    // 시간대 배열 생성 (예: 09:00, 09:30, ..., 18:00)
    //TODO : 장소 운영 시간 설정 부분
    let startHour = 9; // 시작 시간
    let endHour = 18; // 종료 시간
    let currentTime = new Date(date.getFullYear(), date.getMonth(), date.getDate(), startHour, 0);

    while (currentTime.getHours() < endHour) {
        const timeSlot = document.createElement('div');
        timeSlot.classList.add('time-slot');
        timeSlot.textContent = format(currentTime, 'HH:mm');

        // 현재 시간 이전의 슬롯 비활성화
        const slotDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), currentTime.getHours(), currentTime.getMinutes());
        if (slotDate < now) {
            timeSlot.classList.add('disabled');
            timeSlot.style.cursor = 'not-allowed';
        } else {
            timeSlot.addEventListener('click', function () {
                document.querySelectorAll('#timeSlotsContainer .time-slot').forEach(slot => slot.classList.remove('selected'));
                timeSlot.classList.add('selected');

                // 선택한 시간을 숨겨진 입력 필드에 저장
                const selectedTimeInput = document.getElementById('selectedTime');
                if (selectedTimeInput) {
                    selectedTimeInput.value = format(slotDate, 'HH:mm'); // 서버로 전송할 시간 포맷 설정
                }
            });
        }

        timeSlotsContainer.appendChild(timeSlot);

        // 30분 간격으로 시간 추가
        currentTime = new Date(currentTime.getTime() + 30 * 60000);
    }
}

// 날짜 선택 시 호출되는 함수
function handleDateClick(day) {
    const date = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);
    updateAvailableTimes(date);

    // 선택한 날짜를 숨겨진 입력 필드에 저장
    const selectedDateInput = document.getElementById('selectedDate');
    if (selectedDateInput) {
        selectedDateInput.value = format(date, 'yyyy-MM-dd'); // 서버로 전송할 날짜 포맷 설정
    }
}

// 날짜 클릭 이벤트 핸들러 추가
function addDateClickHandlers() {
    document.querySelectorAll('#calendarBody td:not(.disabled)').forEach(td => {
        td.addEventListener('click', function () {
            const day = parseInt(this.textContent, 10);
            handleDateClick(day);
        });
    });
}


const weekDays = ['일', '월', '화', '수', '목', '금', '토'];
const apm = ['오전', '오후'];
let currentDate = new Date();

function generateCalendar(date) {
    const calendarBody = document.getElementById('calendarBody');
    const currentMonth = document.getElementById('currentMonth');

    if (!calendarBody || !currentMonth) {
        console.error("Required elements are not found in the HTML.");
        return;
    }

    calendarBody.innerHTML = ''; // 이전 달력 내용 제거

    const startOfMonthDate = startOfMonth(date); // 해당 월의 첫째 날
    const endOfMonthDate = endOfMonth(date); // 해당 월의 마지막 날
    const startDay = getDay(startOfMonthDate); // 첫째 날의 요일
    const daysInMonth = getDaysInMonth(date); // 해당 월의 일 수

    let row = document.createElement('tr');

    // 이전 월의 빈 칸 생성
    for (let i = 0; i < startDay; i++) {
        row.appendChild(document.createElement('td'));
    }

    // 해당 월의 날짜 생성
    for (let day = 1; day <= daysInMonth; day++) {
        const cell = document.createElement('td');
        cell.textContent = day;

        const currentDay = new Date(date.getFullYear(), date.getMonth(), day);

        // 오늘 날짜 강조
        if (isToday(currentDay)) {
            cell.classList.add('today');
        }

        // 오늘 날짜 이전의 날짜 비활성화
        if (isBefore(currentDay, startOfDay(new Date()))) {
            cell.classList.add('disabled');
            cell.style.cursor = 'not-allowed'; // 커서 모양 변경
            cell.addEventListener('click', function (event) {
                event.preventDefault(); // 클릭 이벤트 방지
            });
        } else {
            cell.addEventListener('click', function () {
                // 날짜 선택 시 스타일 변경
                document.querySelectorAll('#calendarBody td').forEach(td => td.classList.remove('selected'));
                cell.classList.add('selected');

                handleDateClick(day);
            });
        }

        row.appendChild(cell);

        // 주마다 새로운 행 추가
        if ((day + startDay) % 7 === 0) {
            calendarBody.appendChild(row);
            row = document.createElement('tr');
        }
    }

    // 마지막 행 추가
    calendarBody.appendChild(row);

    // 현재 월/년 표시
    currentMonth.textContent = format(date, 'yyyy.MM');

    // 날짜 클릭 핸들러 추가
    addDateClickHandlers();
}

function previousMonth() {
    currentDate = subMonths(currentDate, 1);
    generateCalendar(currentDate);
}

function nextMonth() {
    currentDate = addMonths(currentDate, 1);
    generateCalendar(currentDate);
}

// 현재 시간을 표시하는 함수
function updateCurrentTime() {
    const currentTimeElement = document.getElementById('currentTime');
    if (currentTimeElement) {
        const now = new Date();
        const dayIndex = now.getDay();
        const amPmIdx = (format(now, 'a') == 'AM') ? 0 : 1;
        currentTimeElement.textContent = format(now, `MM.dd (${weekDays[dayIndex]}) ${apm[amPmIdx]} hh:mm`);
    }
}

//*****************************************************************************************************************************

// 초기 달력 생성
generateCalendar(currentDate);

// 현재 시간 업데이트 및 1분마다 실행
updateCurrentTime();
setInterval(updateCurrentTime, 60000);

// 전역 함수로 정의하여 버튼 클릭 시 동작하도록 설정
window.previousMonth = previousMonth;
window.nextMonth = nextMonth;