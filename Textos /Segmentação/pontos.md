

# 1 Introdução
	* Descrição das atas
	* Descrição da tarefa de segmentação
	* Utilidade da segmentação (domínios onde é aplicada)
	* Utilidade da segmentação (Vantagens da utilização para esses domínios)
	* Ideia básica dos algorítmos (Coesão léxica ) como **presuposto básico**
	* Objetivo do Trabalho
		Proposta: Adaptar e Avaliar Texting e C99 para PtBr e Atas
		Dividir o documento de modo que cada parte corresponda a um tópico
	

# 2 Trabalhos Relacionados 

	- Pressuposto da coesão léxica

	- TextTiling

		 [Top Seg for Arabic mostra 3 fases do TextTiling]	

	- C99
		. Baseado em coesão léxica
		. Usa cosine
		. Melhorado com LSA

		 [usa todas as palavras e sem stemming]
		 [Top Seg for Arabic mostra 4 fases do C99]	
	

# 3 Análise dos Resultados

# 4 Avaliação
	- Medidas
		* Tradicionais
		- Pk
		- WindowDiff
			* Problemas do Pk (demasiada penalização de FN / desconsideração de _near misses_)
			* Ideia da media (quantidade de borders por janala)

	[Nas reuniões transcritas ha uma tolerânica de n seundos quando se apura os matches]
		
# 5 Proposta
	- Justificativa / Motivação
		- No problema de recuperação de informação, há duas princias questões: 1 detectar onde há uma divisão entre os tópicos, 2. detectar do que se trata o tópico. Nesse trabalho foca na primeira questão
	- Algoritmos base
	- Adapatações nos alg. base para o contexto das atas
	- Preprocessamento
		- Extração de palavras
		- eliminação de stopwords
		- stemming
		{imagem mostrando o preprocessamento}

# 6 Conclusão

# 7 Referências


[em atas, o vocabulário de segmentos distintos estão relacionados, isto é, compartilham certo vocabulário]


