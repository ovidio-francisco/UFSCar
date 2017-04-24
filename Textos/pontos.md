
## Vanategens e Utilidades da Segmentação Textual
  - Em texto sem indicações de quebras de assutno
  - Utilizada em sumarização de textos e recuperação de informação
  - Aprimpora a consulta
    - Oferece porçoes menores de texto mais relevantes
  - Como etapa de preprocessamento em aplicações voltadas ao entendimento do texto
  - Aprimora a navegação pelo documento, em especial na utilização por usuários em deficiência visual

### Vantagens da Segmentação de Atas de Reunião
  - Frequentemente apresentam pouca marcações da estrutura do documento, como parágrafos, seções ou quaisquer indicações sobre o tema do texto.
  - Estilo de escrita compacto e formal, que desfavorece o processamento.



* * *


## 1 Introdução
* Descrição das atas
  * Descrição da tarefa de segmentação
  * Utilidade da segmentação (domínios onde é aplicada)
  * Utilidade da segmentação (Vantagens da utilização para esses domínios)
  * Objetivo do Trabalho
    * Proposta: Adaptar e Avaliar Texting e C99 para PtBr e Atas
    * Dividir o documento de modo que cada parte corresponda a um tópico

## 2 Trabalhos Relacionados

* Ideia básica dos algorítmos (Coesão léxica ) como **presuposto básico**
* Similaridade e Cosine

  - TextTiling

    [Top Seg for Arabic mostra 3 fases do TextTiling]	

  - C99
    . Baseado em coesão léxica
    . Usa cosine
    . Melhorado com LSA

    [usa todas as palavras e sem stemming]
    [Top Seg for Arabic mostra 4 fases do C99]	
	

## 3 Adapatção às Atas

  * Algoritmos tradicionais feitos para o inglês
  * Dificuldade: Coesão léxica não tão bem definida
  * Dificuldade: estilo da escrita
    - Paragrafo único
    - Cabeçalhos e rodapés
    - Pontuação --> ';' encerrando sentenças
    - Insersão de espaços que não são quebra de sentença
    - Ruídos

### 		Preprocessamento

  * Stop words e stemming
  * Imagem exemplo
	
### 		Remoção de ruídos
	
  * Cabeçalhos e rodapés
  * Numerais	

### 		Identificação de Candidatos


## 4 Avaliação
  - Medidas
  * Tradicionais
  * Para Segmentações
      - Pk
      - WindowDiff
          * Problemas do Pk (demasiada penalização de FN / desconsideração de _near misses_)
          * Ideia da media (quantidade de borders por janala)

	[Nas reuniões transcritas ha uma tolerânica de n seundos quando se apura os matches]
	
	
	
	
		
## 5 Proposta
  - Justificativa / Motivação
    - No problema de recuperação de informação, há duas princias questões: 1 detectar onde há uma divisão entre os tópicos, 2. detectar do que se trata o tópico. Nesse trabalho foca na primeira questão
  - Algoritmos base
  - Adapatações nos alg. base para o contexto das atas
  - Preprocessamento
    - Extração de palavras
    - eliminação de stopwords
    - stemming
    {imagem mostrando o preprocessamento}

## 6 Conclusão

## 7 Referências


[em atas, o vocabulário de segmentos distintos estão relacionados, isto é, compartilham certo vocabulário]


