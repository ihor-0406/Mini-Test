
# Збірка та запуск

docker compose build
docker compose up -d

# Перевірка стану

docker compose ps
docker logs -f auth-api
docker logs -f data-api

# Зупинка: 

docker compose down


# ===================1) Реєстрація та логін (отримуємо токен)==================================

curl -s -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"a@a.com","password":"pass"}'

TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"a@a.com","password":"pass"}' | jq -r .token)
echo $TOKEN

# 2) =================Сквозний виклик через auth-api -> data-api=================================

curl -s -X POST http://localhost:8080/api/process \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{"text":"hello"}'
# очікувано: OLLEH

# 3)=====================Прямий виклик data-api (для відладки)==================================

curl -s -X POST http://localhost:8081/api/transform \
-H "X-Internal-Token: dev-secret" \
-H "Content-Type: application/json" \
-d '{"text":"hello"}'
# очікувано: OLLEH

