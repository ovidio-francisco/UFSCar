


# Segmentação Textual Automática de Atas de Reunião



## Resumo
    - Breve Definição de segmentação.
    - Literatura focada em documentos longos e em inglês.
    - Maior foco e performance no inglês.
    - Peculiaridades das atas.
    - Utilidade da segmentação
    - Objetivo: Adaptar algs e encotrar modelo
    - Modelo melhor avaliado


## 1 - Introdução
    - Descrição das Reuniões
    - Atas são documentos não estruturados e dificuldades para o sistema 
    - Segmentar e extrair tópicos
    - Definição da tarefa de segmentação
    - Utilidade da segmentação 
        - domínios onde e aplicada
        - interesse crescente
        - aprimoramento a: acesso a informação, sumarização de textos, acessibilidade
    - Descrição das atas
    - Onde é usada e lacunas
    - Foco nas atas
    - Ornanizado do restante do artigo


## 2 - Referencial Teórico
    - Retomada de definições
        - Segmentação
        - Segmento
        - Coesão léxica 
        - Coseno

### Principais algortimos
    - TextTiling
        - Funcionamento
        - Janela deslizante
        - Similaridade entre janelas
        - Os limites estão nos picos de dissimilaridade
        - Baixa complexidade e baixa eficiência
    - C99
        - Funcionamento
        - Usa matrizes de similaridas mas o autor aponta problemas:
            - Para Pequenos segmentos o cálculo de similaridas não é confiável
            - Textos iniciais costumam ter baixa coesão léxica
            - Comparar distantes não é apropriado
        - Criação da matriz de ranking
        - Figura mostrando a criação da matriz de ranks
        - Divisão por clustering para encontrar os limites

### Medidas de avaliação
    - Medidas tradicionais, baseian-se em hits para computar os erros
    - Falsos positivos e falsos negativos
    - Precisão e revocação trazem alguns problemas
        - Mais segmentos identicados, melhora revocação e reduz precisão
        - F1 é mais difícil de interpretar
        - Falta de sencibilidade à near misses
        - Figura mostrando exemplo de near misses
    - Principais medidas propostas
        - Pk
            - Funcionamento
            - Move uma janela ao longo da sementação referência e hipótese
            - Verifica para ref. e hip se o início e o final da janela estão no mesmo segmento.
            - Computa um erro em case de discordância entre ref. e hip.
            




## 3 - Trabalhos Relacionados



## 4 - Conjunto de Documentos



## 5 - Proposta: Segmentação Linear Automática de Atas de Reunião 



## 6 - Avaliação Experimental



## 7 - Conclusão






   
