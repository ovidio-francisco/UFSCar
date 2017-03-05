
# Pauta 

	- Especialistas
	  - Retorno do Fábio e Luciana

	- Leitura dos 7 artigos sobre segmentação

	- Adaptação do JTextTiling 
	  - Stemmer Português
	  - Lista StopWords Português	
	  - Remoção de Numeros, simbolos e palavras pequenas
	  - Identificação de Finais de Sentença
	  - Identificação de Cabeçalhos
	  - Interface

	- Avaliação
	  - Os autores criam um 'Gold Text' - que é um texto com segmentos validados
	    - Concatenação de Textos
	    - Transcrição de audio onde há um moderador marcando o tempo que o assunto é mudado
	  - Matches
	    - Juizes avaliam se concordam ou não.
	    - Não se avalia o segmentador diretamente, mas o resultado da aplicação final.
	    - WindowDiff	


	- Funcionamento do TextTiling
	  - Recebe um texto e encontra 'candidate boundaries'
	  - Analisa o texto ao redor de cada candidate boundarie e,
	  - Cada cadidate boundarie recebe um score que a probabilidade de ali haver uma troca de tópicos




# Tarefas

	1. Avaliar TextTiling e C99 com os Especialistas, Usar Hits/Matches com WindowDiff.
	2. Apresentar um Cronograma.

