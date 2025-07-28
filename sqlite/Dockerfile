FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o JAR da aplicação
COPY target/sqlite-app.jar app.jar

# Criar diretório para dados
RUN mkdir -p /app/data

# Expor porta
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"] 