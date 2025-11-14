document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const messageDiv = document.getElementById('message');

    // Показать сообщение
    function showMessage(text, type) {
        messageDiv.textContent = text;
        messageDiv.className = `message ${type}`;
        messageDiv.classList.remove('hidden');

        // Скрыть сообщение через 3 секунды
        setTimeout(() => {
            messageDiv.classList.add('hidden');
        }, 3000);
    }

    // Обработка отправки формы
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;


        // Имитация загрузки
        const submitBtn = loginForm.querySelector('.login-btn');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = originalText+'...';
        submitBtn.disabled = true;
        fetch("${context}/doLogin", {
            method: 'POST',
            body: JSON.stringify({login: username, password: password})
        }).then(async (resp) =>{
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
            const json = await resp.json()
            if(json.success){
                window.location.href = window.tempUrl;
                return;
            }
            showMessage(json.errorMessage, 'error')
        }, async ()=>{
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
            showMessage('Unknown error', 'error');
        })
    });

    // Дополнительная валидация в реальном времени
    const inputs = loginForm.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            if (this.value.trim() !== '') {
                this.style.borderColor = '#667eea';
            } else {
                this.style.borderColor = '#ddd';
            }
        });
    });
});