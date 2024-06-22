# Используем официальный образ OpenJDK как базовый
FROM openjdk:18

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем jar файл Ktor проекта в контейнер
COPY build/libs/com.example.streamed-all.jar /app/com.example.streamed-all.jar

# Указываем команду запуска Ktor приложения
CMD ["java", "-jar", "/app/com.example.streamed-all.jar"]

# Указываем порт, который будет использовать приложение
EXPOSE 8080
