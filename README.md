# Filmorate
## Описание приложения
Представьте, что Вы решили отдохнуть и провести вечер за просмотром фильма. Вкусная еда уже готовится, любимый плед уютно свернулся на кресле — а вы всё ещё не выбрали, что же посмотреть! Фильмов много — и с каждым годом становится всё больше. Чем их больше, тем больше разных оценок. Чем больше оценок, тем сложнее сделать выбор. Сервис Filmorate работает с фильмами и оценками пользователей, а также возвращает фильмы, рекомендованные к просмотру. Теперь ни Вам, ни вашим друзьям не придётся долго размышлять, что посмотреть вечером. Рейтинг фильмов составляется на основе отзывов пользователей.

Разработка на Spring Boot.

## Параметры:

group: ru.yandex.practicum;  
artifact: filmorate;  
name: filmorate;  
dependencies: Spring Web.  

![diagram](https://github.com/Julia3202/java-filmorate/blob/main/BD%20filmorate.jpg)

## Описание диаграммы:
1. Films – информация о фильмах.
* film_id – идентификатор фильма.
* film_name – название фильма.
* film_description – описание фильма.
* release_date – дата выхода.
* duration – продолжительность.
* likes – количество лайков.
* mpa_id – идентификатор рейтинга Ассоциации кинокомпаний.
2. MPA – наименования рейтингов фильмов Ассоциации кинокомпаний.
* mpa_id – идентификатор рейтинга.
* mpa_name – наименование рейтинга.
3. Genre- наименования жанров фильмов.
* genre_id – идентификатор жанра.
* genre_name – наименование жанра.
4. film_like – ID пользователей,поставивших лайк фильму.
* film_id – идентификатор фильма.
* user_id – идентификатор пользователя.
5. Users – информация о пользователях.
* user_id – идентификатор пользователя.
* user_name – имя пользователя.
* user_login – логин.
* user_email – адрес электронной почты.
* user_birthday – день рождения пользователя.
6. User_friend – ID пользователей и ID их друзей.
* user_id – идентификатор пользователя.
* friend_id – идентификатор друга пользователя.
7. Film_genre – ID фильмов и жанров этих фильмов.
* genre_id – идентификатор жанра.
* film_id – идентификатор фильма.

# REST-контроллеры
## Классы-контроллеры:

FilmController обслуживает фильмы,  
UserController — пользователей,  
GenreController – жанры,  
DirectorController – режиссеров,  
MpaCategoryController – категории,  
ReviewController - отзывы.  
В классах-контроллерах имеются эндпоинты с подходящим типом запроса для каждого из случаев.

## Для FilmController:

POST /films - добавление фильма.  
PUT /films - обновление фильма.  
GET /films - получение всех фильмов.  
GET /films/{filmId} – получение фильма по идентификатору.  
DELETE /films/{filmId} – удаление фильма.  
PUT /films/{filmId}/like/{userId} — пользователь ставит лайк фильму.  
DELETE /films/{filmId}/like/{userId} — пользователь удаляет лайк.  
GET /films/{filmId}/likes – список пользователей лайкнувших фильм.  
GET /films/{filmId}/genres – список жанров фильма.  
GET /films/popular?count={count}&genreId={genreId}&year={year} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, вернутся первые 10. Фильтрация осуществляется по двум параметрам: по жанру, за указанный год.  
GET /films/director/{directorId}?sortBy=[year,likes] – Возвращает список фильмов режиссера отсортированных по количеству лайков или году выпуска.  
GET /films/search?query={query}&by={director,title} - список фильмов, отсортированных по популярности – результат поиска по названию фильмов и по режиссёру, query — текст для поиска, by — может принимать значения director (поиск по режиссёру), title (поиск по названию), либо оба значения через запятую при поиске одновременно и по режиссеру и по названию.  
GET /films/common?userId={userId}&friendId={friendId} - возвращает список фильмов, отсортированных по популярности. Параметры: userId — идентификатор пользователя, запрашивающего информацию; friendId — идентификатор пользователя, с которым необходимо сравнить список фильмов.  

## Для UserController:

POST /users - создание пользователя.  
PUT /users - обновление пользователя.  
DELETE /users/{userId} – удаление пользователя.  
GET /users/{userId} - получение данных о конкретном пользователе.  
GET /users - получение списка всех пользователей.  
PUT /users/{userId}/friends/{friendId} — добавление в друзья.  
DELETE /users/{userId}/friends/{friendId} — удаление из друзей.  
GET /users/{userId}/friends — возвращает список пользователей, являющихся его друзьями.  
GET /users/{userId}/friends/common/{otherId} — список друзей, общих с другим пользователем.  
GET /users//{userId}/feed - получение списка списка событий, которые пользователь совершил на платформе.  
GET /users/{userId}/recommendations – список рекомендаций.  

## Для MpaCategoryController:

GET /mpa – получение всех категорий MPA.  
GET /mpa/{id} – получение категории по идентификатору.  
POST /mpa - создание категории.  

## Для GenreController:

GET /genres – получение списка всех жанров.  
GET /genres/{id} – получение жанра по идентификатору.  
POST /genres – создание жанра.  

# Для ReviewController:

POST /reviews - добавление нового отзыва.  
PUT /reviews - редактирование уже имеющегося отзыва.  
DELETE /reviews/{id} - удаление уже имеющегося отзыва.  
GET /reviews/{id} - получение отзыва по идентификатору.  
GET /reviews?filmId={filmId}&count={count} - получение всех отзывов по идентификатору фильма, если фильм не указан то получение всех фильмов. Если кол-во не указано, то 10.  
PUT /reviews/{id}/like/{userId} - пользователь ставит лайк отзыву.  
PUT /reviews/{id}/dislike/{userId} - пользователь ставит дизлайк отзыву.  
DELETE /reviews/{id}/like/{userId} - пользователь удаляет лайк/дизлайк отзыву.  
DELETE /reviews/{id}/dislike/{userId} - пользователь удаляет дизлайк отзыву.  
Эндпоинты для создания и обновления данных должны также вернуть созданную или изменённую сущность.  

# Бизнес логика  
Пользователям должны иметь возможность добавлять друг друга в друзья и ставить фильмам лайки.

Бизнес-логика объединена в пакете service. Кроме основных функций добавления / изменения / удаления, классы этого пакета поддерживают дополнительные возможности.

# Логирование  
Логирование осуществляется в слое контроллеров, при получении данных из базы данных, при возникновении ошибок — например, если валидация не пройдена.

# Тестирование  
Тесты для валидации пользователя и фильма. Подтверждают, что она работает на граничных условиях. Интеграционные тесты для проверки взаимодействия с базой данных.

# Примеры запросов:

1. Получить список всех фильмов:
select f.FILM_ID, 
f.FILM_NAME, 
f.FILM_DESCRIPTION, 
f.RELEASE_DATE, 
f.DURATION, 
f.LIKES,
M.MPA_ID, 
M.MPA_NAME 
from FILMS f 
INNER JOIN MPA M on M.MPA_ID = f.MPA_ID;

2. Получить список друзей пользователя с id=2:
select u.* 
from USER_FRIEND uf 
inner join USERS u on uf.FRIEND_ID = u.USER_ID 
where uf.USER_ID =2; 

3. Удалить пользователя из списка друзей(user_id=1 , friend_id=2):
delete 
from USER_FRIEND 
where USER_ID = 1 and FRIEND_ID = 2;







