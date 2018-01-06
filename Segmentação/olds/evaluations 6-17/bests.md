# Primeiros Testes com 20 modelos para cada medida

## Without MyPreprocess

						best			| 	média do best
wpC99 Acuracy			C9-60-9-t		| 	0.588	
wpC99 Precision			C9-40-9-t		| 	0.645	
wpC99 Recall			C9-80-9-t       |   0.869
wpC99 F1				C9-80-9-t		| 	0.638	
wpC99 Pk				C9-20-9-t		| 	0.134	
wpC99 Windiff			C9-60-9-t		| 	0.411	
---------------------------------------------------------
wpTextTiling Acuracy	TT-50-6			| 	0.612	
wpTextTiling Precision	TT-40-9			|	0.611	
wpTextTiling Recall		TT-20-3			| 	0.886	
wpTextTiling F1			TT-30-6			| 	0.605	
wpTextTiling Pk			TT-50-9			| 	0.142	
wpTextTiling Windiff	TT-50-6			| 	0.387	


## With MyPreprocess
	
						best			| 	média do best
ppC99 Acuracy			C9-60-9-t		| 	0.609
ppC99 Precision			C9-20-11-f		| 	0.720	
ppC99 Recall			C9-80-11-t		| 	0.897	
ppC99 F1				C9-80-11-t		| 	0.655	
ppC99 Pk				C9-20-11-f		| 	0.116	
ppC99 Windiff			C9-60-9-t		| 	0.390 	
---------------------------------------------------------
ppTextTiling Acuracy	TT-40-9			| 	0.603	
ppTextTiling Precision	TT-50-12		| 	0.613
ppTextTiling Recall		TT-20-3			| 	0.917	
ppTextTiling F1			TT-40-3			| 	0.648	
ppTextTiling Pk			TT-50-9			| 	0.144
ppTextTiling Windiff	TT-40-9			| 	0.396	



# Teste final com os 4 resultados de cada medida (TextTiling e C99, com e sem preprocess)


## Acuracy
ppC99 Acuracy			C9-60-9-t		| 	0.609
ppTextTiling Acuracy	TT-40-9			| 	0.603	
wpC99 Acuracy			C9-60-9-t		| 	0.588	
wpTextTiling Acuracy	TT-50-6			| 	0.612	

## F1
ppC99 F1				C9-80-11-t		| 	0.655	
ppTextTiling F1			TT-40-3			| 	0.648	
wpC99 F1				C9-80-9-t		| 	0.638	
wpTextTiling F1			TT-30-6			| 	0.605	

## Precision
ppC99 Precision			C9-20-11-f		| 	0.720	
ppTextTiling Precision	TT-50-12		| 	0.613
wpC99 Precision			C9-40-9-t		| 	0.645	
wpTextTiling Precision	TT-40-9			|	0.611	

## Recall
ppC99 Recall			C9-80-11-t		| 	0.897	
ppTextTiling Recall		TT-20-3			| 	0.917	
wpC99 Recall			C9-80-9-t       |   0.869
wpTextTiling Recall		TT-20-3			| 	0.886	

## Pk
ppC99 Pk				C9-20-11-f		| 	0.116	
ppTextTiling Pk			TT-50-9			| 	0.144
wpC99 Pk				C9-20-9-t		| 	0.134	
wpTextTiling Pk			TT-50-9			| 	0.142	

## Windiff
ppC99 Windiff			C9-60-9-t		| 	0.390 	
ppTextTiling Windiff	TT-40-9			| 	0.396	
wpC99 Windiff			C9-60-9-t		| 	0.411	
wpTextTiling Windiff	TT-50-6			| 	0.387	



# Resultado Final ===================

## Acuracy               C.60.9.t.T
ppC99 Acuracy			C9-60-9-t		| 	0.609

## F1                    C.80.11.t.T
ppC99 F1				C9-80-11-t		| 	0.655	

## Precision             C.20.11.f.T
ppC99 Precision			C9-20-11-f		| 	0.720	

## Recall               TT.20.3.T
ppTextTiling Recall		TT-20-3			| 	0.917	

## Pk                    C.20.11.f.T
ppC99 Pk				C9-20-11-f		| 	0.116	

## Windiff               C.60.9.t.T
ppC99 Windiff			C9-60-9-t		| 	0.390 	






