const weekDays = ['일', '월', '화', '수', '목', '금', '토'];
const apm = ['오전', '오후'];

function getReservationDateAndTime() {
    const reservationDateElement = document.getElementById('reservationDateAndTime');

    if (reservationDateElement) {
        const selectDate = reservationDateElement.getAttribute('data-date');
        const selectTime = reservationDateElement.getAttribute('data-time');

        const date = new Date(selectDate + 'T' + selectTime);
        const dayOfWeek = weekDays[date.getDay()];
        const hour = date.getHours();
        const minutes = date.getMinutes();
        const formattedHour = hour > 12 ? hour - 12 : hour;
        const period = apm[hour >= 12 ? 1 : 0];

        const formattedDate = `${selectDate.replace(/-/g, '.')}(${dayOfWeek}) ${period} ${formattedHour}:${minutes.toString().padStart(2, '0')}`;

        reservationDateElement.innerText = `예약시간: ${formattedDate}`;
    }
}

// 페이지 로드 후 숫자를 포맷
document.addEventListener('DOMContentLoaded', () => {
    const totalPriceElement = document.getElementById('totalPrice');
    let getPrice = totalPriceElement.getAttribute('data-totalPrice');
    const totalPrice = parseFloat(getPrice);

    totalPriceElement.textContent = '매장에서 결제할 금액: ' + totalPrice.toLocaleString() + '원';
});

getReservationDateAndTime();