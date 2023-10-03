# KtorNotes
Приложение для создания, редактирования и удаленного хранения заметок с API на Ktor и JWT-аутентификацией. (Kotlin, Jetpack Compose, Clean Architecture, Retrofit)

Бэкенд: https://github.com/hey-vic/ktor-notes

<img src="https://github.com/hey-vic/KtorNotes/blob/media/app-gif.gif" width="360" height="640"/>

## Стек
- Kotlin (Coroutines, Flow)
- Jetpack Compose
- MVVM, Clean Architecture
- Retrofit + OkHttp
- Dagger-Hilt
- Material Design
- Бэкенд на Ktor + MongoDB + JWT-аутентификация

## Скриншоты
![image](https://github.com/hey-vic/KtorNotes/assets/58303400/92cf87d6-74bb-4e94-9764-d559d559f5a5)
![image](https://github.com/hey-vic/KtorNotes/assets/58303400/2792db52-35bf-4ae0-981b-a1b17a0e484c)

## Функции
- Регистрация и вход по почте и паролю
- Отображение списка заметок для текущего пользователя, возможность просмотреть отдельную заметку
- Добавление, редактирование, удаление заметок
- После авторизации токен сохраняется в Shared Preferences, при следующем входе в приложение открывается уже не экран авторизации, а сразу экран заметок
- Возможность выхода из аккаунта (удаление токена из Shared Preferences, возврат на экран авторизации)
- Проверка полей на валидность, вывод соответствующих сообщений об ошибках (почта не соответствует формату, пароль слишком короткий, пароль неверен, попытка повторной регистрации
на ту же почту, поля заметки пустые)
- Вывод сообщения об ошибке при отстутствующем интернет-соединении
- Возможность скрыть / показать пароль
- Темная тема
