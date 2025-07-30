#!/bin/bash

# Script para executar testes da API PetShop
# Uso: ./run-tests.sh [opção]

echo "=== Testes da API PetShop ==="
echo ""

# Verificar se MongoDB está rodando
echo "Verificando MongoDB..."
if ! pgrep -x "mongod" > /dev/null; then
    echo "❌ MongoDB não está rodando. Iniciando..."
    sudo systemctl start mongodb
    sleep 2
else
    echo "✅ MongoDB está rodando"
fi

# Verificar se Redis está rodando
echo "Verificando Redis..."
if ! pgrep -x "redis-server" > /dev/null; then
    echo "❌ Redis não está rodando. Iniciando..."
    sudo systemctl start redis-server
    sleep 2
else
    echo "✅ Redis está rodando"
fi

echo ""

# Função para executar testes
run_tests() {
    case $1 in
        "all")
            echo "Executando todos os testes..."
            mvn clean test
            ;;
        "unit")
            echo "Executando testes unitários..."
            mvn test -Dtest="*Test" -Dtest=*Model*,*RepositoryTest,*ControllerTest,*ServiceTest
            ;;
        "integration")
            echo "Executando testes de integração..."
            mvn test -Dtest="*IntegrationTest"
            ;;
        "coverage")
            echo "Executando testes com cobertura..."
            mvn clean test jacoco:report
            echo "Relatório de cobertura gerado em: target/site/jacoco/index.html"
            ;;
        "debug")
            echo "Executando testes em modo debug..."
            mvn test -Dspring.profiles.active=test -Dlogging.level.com.grupo7.api=DEBUG
            ;;
        "quick")
            echo "Executando testes rápidos (sem integração)..."
            mvn test -Dtest="*Test" -Dtest=*Model*,*RepositoryTest,*ControllerTest,*ServiceTest -Dspring.profiles.active=test
            ;;
        *)
            echo "Uso: $0 [opção]"
            echo ""
            echo "Opções disponíveis:"
            echo "  all        - Executar todos os testes"
            echo "  unit       - Executar apenas testes unitários"
            echo "  integration- Executar apenas testes de integração"
            echo "  coverage   - Executar testes com relatório de cobertura"
            echo "  debug      - Executar testes em modo debug"
            echo "  quick      - Executar testes rápidos (sem integração)"
            echo ""
            echo "Exemplo: $0 all"
            exit 1
            ;;
    esac
}

# Executar testes baseado no argumento
if [ $# -eq 0 ]; then
    echo "Executando todos os testes..."
    run_tests "all"
else
    run_tests "$1"
fi

echo ""
echo "=== Testes concluídos ===" 