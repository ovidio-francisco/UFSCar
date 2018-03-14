-------------------------------------------------------------------------
-------------------------------------------------------------------------

Avaliação Experimental

Neste capítulo, o processo de aprendizado de hierarquias de tópicos a partir de coleções
textuais dinâmicas, com uso de algoritmos de agrupamento incremental, é avaliado ex-
perimentalmente. A avaliação experimental, neste trabalho, é focada em três pontos:

Comparar os algoritmos de agrupamento incremental Leader, DCTree, Buckshot e
IHTC, na tarefa de obtenção da representação condensada dos textos;

Analisar a ecácia de recuperação dos agrupamentos hierárquicos construídos a
partir das representações condensadas dos textos.
Ainda, compará-los com uma
estratégia de agrupamento hierárquico não incremental; e

Analisar o desempenho dos descritores para recuperar os documentos dos grupos.
Neste caso, o objetivo é comparar a seleção de descritores obtidos pelo IHTC, pro-
posto neste trabalho, com a seleção de descritores baseada em centroides.
Nas próximas seções, são apresentadas as coleções textuais a serem utilizadas na avali-
ação experimental. Em seguida, é descrita a conguração dos experimentos, com o ajuste
de parâmetros dos algoritmos e os critérios de avaliação.
Por m, são apresentados os
experimentos realizados e uma análise dos resultados.


5.2 Conguração dos Experimentos

Para realizar essas avaliações, antes é necessário um procedimento de conguração dos
experimentos para denição dos parâmetros envolvidos no processo. No contexto deste
trabalho, é preciso denir a técnica de pré-processamento dos textos, os parâmetros dos
algoritmos de agrupamento, a técnica de seleção de descritores para o agrupamento e, por
m, os critérios a serem utilizados na avaliação dos resultados.

Para a conguração dos experimentos, neste trabalho, o pré-processamento dos textos
deve ser adequado para o ...

... Para realização de testes de signicância estatística e construção dos rankings de desempenho
dos algoritmos, foi utilizado o software KEEL Data Mining (Alcalá et al., 2011).


-------------------------------------------------------------------------
-------------------------------------------------------------------------

Atualmente, estão disponíveis em diversas linguagens de programação,
bibliotecas que implementam algoritmos de modelagem de tópicos para a extração de
tópicos como nos exemplos vistos. Dentre elas, pode-se destacar a “lda-c” 2 para a
linguagem C, “mallet” 3 para Java e “gensim” 4 para Python, porém muitas outras
linguagens já possuem os algoritmos implementados. Devido ao seu caráter probabilístico, não é necessário realizar nenhum pré-processamento complexo no corpus
para a utilização, bastando uma simples tokenização do conteúdo textual. As palavras
comuns são irrelevantes para o resultado da execução e quaisquer demais processamentos
podem ser utilizados como forma de limpeza ou de melhorar desempenho, porém
opcionais.


4.3.3.1 Ranqueamento
As principais técnicas apresentadas aqui são: A Frequência de termos (tf), a
Relação Grau/Frequência (deg/tf) [Berry 2010] e o Grau modificado de rótulo
[Nolasco e Oliveira 2016].


file:///ext4Data/UFSCar/papers/Tópicos/Modelagem de Tópicos e Criação de Rótulos - Identificando Temas em Dados Semi-Estruturados e Não-Estruturados.pdf


--------------------------------------------------


file:///ext4Data/UFSCar/papers/clustering/A Segment-based Approach To Clustering Multi-Topic Documents.pdf
https://github.com/rashmigulhane/soft-clustering-of-multi-topic-documents
file:///ext4Data/UFSCar/papers/Segmentação/Multi-document Topic Segmentation.pdf
file:///ext4Data/UFSCar/papers/Segmentação/Statistical topic models for multi-label document classification.pdf
file:///ext4Data/UFSCar/papers/dissertações/dissertacao_bruno_schneider.pdf.pdf
file:///ext4Data/UFSCar/papers/Segmentação/Multi-Topic Multi-Document Summarizer.pdf

Multi-topic Aspects in Clinical Text Classification
Learning from multi-topic web documents for contextual advertisement



--------------------




