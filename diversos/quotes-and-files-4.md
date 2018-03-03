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



