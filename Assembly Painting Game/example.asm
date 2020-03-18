.386
.model flat, stdcall
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;includem biblioteci, si declaram ce functii vrem sa importam
includelib msvcrt.lib
extern exit: proc
extern malloc: proc
extern memset: proc

includelib canvas.lib
extern BeginDrawing: proc
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;declaram simbolul start ca public - de acolo incepe executia
public start
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;sectiunile programului, date, respectiv cod
.data
;aici declaram date
dreptunghi dd 0
dreapta dd 0
cerc dd 0
clear dd 0

	x dd 340 ; center x
	y dd 700 ; center y
	r dd 1 ; raza
	w dd 0
	h dd 0
	dx1 dd 0
	dy1 dd 0
	dx2 dd 0
	dy2 dd 0
	longest dd 0
	shortest dd 0
	numarator dd 0
	aux dd 0
	click_1_x dd 0
	click_1_y dd 0
	click_2_x dd 0
	click_2_y dd 0
	contor dd 1
	centru_x dd 0
	centru_y dd 0
	raza dd 0
	i dd 0
	
window_title DB "Exemplu proiect desenare",0
area_width EQU 1400
area_height EQU 680
area_width_casuta equ 110
area_height_casuta equ 100
area DD 0
area1 dd 0
area2 dd 0
area3 dd 0
discr dd 0
cell_height equ 100
cell_width equ 250
alb dd 0FFFFFFh;
albastru dd 00037FFh
movv dd 0B700EAh
galben dd 0FFC228h
curent_color dd -1

aux3 dd 0
poz dd 0	
		
		x_pornire dd 0
		y_pornire dd 0 
    
		x_plus_x dd 0
		x_minus_x dd 0
		y_plus_y dd 0
		y_minus_y dd 0
    
		x_plus_y dd 0
		x_minus_y dd 0
		y_plus_x dd 0
		y_minus_x dd 0
		d dd 1 ;parametru de decizie a urmatorului pixel ce va fi setat
		
counter DD 0 ; numara evenimentele de tip timer
razza dd 0
arg1 EQU 8
arg2 EQU 12
arg3 EQU 16
arg4 EQU 20
arg5 EQU 24

culoare_simb DD 0

symbol_width EQU 10
symbol_height EQU 20
symbol_width_color equ 40
symbol_height_color equ 60

include digits.inc
include letters.inc

.code
; procedura make_text afiseaza o litera sau o cifra la coordonatele date
; arg1 - simbolul de afisat (litera sau cifra)
; arg2 - pointer la vectorul de pixeli
; arg3 - pos_x
; arg4 - pos_y
make_text proc
	push ebp
	mov ebp, esp
	pusha
	
	mov eax, [ebp+arg1] ; citim simbolul de afisat
	cmp eax, 'A'
	jl make_digit
	cmp eax, 'Z'
	jg make_digit
	sub eax, 'A'
	lea esi, letters
	jmp draw_text
make_digit:
	cmp eax, '0'
	jl make_space
	cmp eax, '9'
	jg make_space
	sub eax, '0'
	lea esi, digits
	jmp draw_text
make_space:	
	mov eax, 26 ; de la 0 pana la 25 sunt litere, 26 e space
	lea esi, letters
	
draw_text:
	mov ebx, symbol_width
	mul ebx
	mov ebx, symbol_height
	mul ebx
	add esi, eax
	mov ecx, symbol_height
bucla_simbol_linii:
	mov edi, [ebp+arg2] ; pointer la matricea de pixeli
	mov eax, [ebp+arg4] ; pointer la coord y
	add eax, symbol_height
	sub eax, ecx
	mov ebx, area_width
	mul ebx
	add eax, [ebp+arg3] ; pointer la coord x
	shl eax, 2 ; inmultim cu 4, avem un DWORD per pixel
	add edi, eax
	push ecx
	mov ecx, symbol_width
bucla_simbol_coloane:
	cmp byte ptr [esi], 0
	je simbol_pixel_colorat
	mov dword ptr [edi], 0
	jmp simbol_pixel_next
simbol_pixel_colorat:
	push eax
	mov eax, [ebp+arg5]
	mov dword ptr [edi], eax
	pop eax
simbol_pixel_next:
	inc esi
	add edi, 4
	loop bucla_simbol_coloane
	pop ecx
	loop bucla_simbol_linii
	popa
	mov esp, ebp
	pop ebp
	ret
make_text endp

; un macro ca sa apelam mai usor desenarea simbolului
make_text_macro macro symbol, drawArea, x, y,cc
	push cc
	push y
	push x
	push drawArea
	push symbol
	call make_text
	add esp, 20
endm



deseneaza_linie_orizontala macro x, y
local vf, final
	pusha
	mov eax, 0
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0ff1414h
	inc ecx
	inc ecx
	cmp ecx, 5595
	je final
	loop vf
final:
	popa
endm


deseneaza_linie_orizontala2 macro x, y
local vf, final
	pusha
	mov eax, 0
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0ff1414h
	inc ecx
	inc ecx
	cmp ecx, 400
	je final
	loop vf
final:
	popa
endm


deseneaza_linie_orizontala3 macro x, y
local vf, final
	pusha
	mov eax, 0
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0ff1414h
	inc ecx
	inc ecx
	cmp ecx, 700
	je final
	loop vf
final:
	popa
endm


deseneaza_linie_orizontala_dupa_lungime macro x, y, lungime
local vf, final
	pusha
	mov eax, 0
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0ff1414h
	inc ecx
	inc ecx
	cmp ecx, lungime
	je final
	loop vf
final:
	popa
endm


deseneaza_linie_verticala_dupa_lungime macro x, y, lungime
local vf, final
	pusha
	xor eax, eax
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	add eax, 5599
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0h
	inc ecx
	inc ecx
	
	cmp ecx, lungime
	je final
	loop vf
final:
	popa
endm


deseneaza_linie_verticala macro x, y
local vf, final
	pusha
	xor eax, eax
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	add eax, 5599
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0h
	inc ecx
	inc ecx
	
	cmp ecx, 100
	je final
	loop vf
final:
	popa
endm


deseneaza_linie_verticala2 macro x, y
local vf, final
	pusha
	xor eax, eax
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	add eax, 5599
	mov ebx, [area]
	add ebx, ecx
	mov dword ptr [ebx+eax], 0h
	inc ecx
	inc ecx
	
	cmp ecx, 65
	je final
	loop vf
final:
	popa
endm

	atribuie macro a, b
		pusha
		mov eax, [b]
		mov [a], eax
		popa
		endm
		negare macro a
		pusha
		mov eax, [a]
		neg eax
		mov [a], eax
		popa
		endm
		
		incrementeaza macro a
		pusha
		mov eax, [a]
		inc eax
		mov [a], eax
		popa
		endm
		
		decrementeaza macro a
		pusha
		mov eax, [a]
		dec eax
		mov [a], eax
		popa
		endm
		
		compara macro a, b
		pusha
		mov ecx, [a]
		cmp ecx, [b]
		popa
		endm
		
		compara_variabila_si_numarul macro a, b
		pusha
		mov ecx, [a]
		cmp ecx, b
		popa
		endm

		aduna_si_atribuie macro c, a, b
		pusha
		mov eax, [a]
		add eax, [b]
		mov [c], eax
		popa
		endm 

		scade_si_atribuie macro c, a, b
		pusha
		mov eax, [a]
		sub eax, [b]
		mov [c], eax
		popa
		endm
		
		aduna_3_si_atribuie macro d, a, b, c
		pusha
		mov eax, [a]
		add eax, [b]
		add eax, [c]
		mov [d], eax
		popa
		endm 
		
		scade_3_si_atribuie macro d, a, b, c
		pusha
		mov eax, [a]
		sub eax, [b]
		sub eax, [c]
		mov [d], eax
		popa
		endm
		
		pune_pixel macro x, y
		pusha
	mov eax, 0
	mov eax, x
	mov ebx, area_width
	mul ebx
	add eax, y
	shl eax, 2
	mov ebx, [area]
	mov dword ptr [ebx+eax], 0h
		popa
		endm

		radical macro n,raza
		local return,bucla
		pusha
		bucla:
		mov eax, i
		mul i
		cmp eax, n
		ja return
		inc i
		jmp bucla
		return: 
		dec i
		mov eax, i
		mov raza, eax
		mov i, 0
		popa
		endm

	distanta macro x1,y1,x2,y2
	
	pusha
	mov eax, x1
	sub eax, x2
	mov ebx, eax
	mul ebx
	mov edi, eax
	xor eax, eax
	mov eax, y1
	sub eax, y2
	mov ebx, eax
	mul ebx
	add eax, edi
	mov ecx, eax
	radical ecx,raza
	popa
    endm
		

DeseneazaCerc macro centruX, centruY,razza
		local desenare_cerc_loop,sens_negativ,end_desenare
		pusha
		
		
		atribuie y_pornire, razza ;pornim din punctul (0,raza) 
		atribuie d, razza
		Negare d
		desenare_cerc_loop:
				 aduna_si_atribuie x_plus_x, centruX, x_pornire
				 scade_si_atribuie x_minus_x, centruX, x_pornire
				 aduna_si_atribuie y_plus_y, centruY, y_pornire
				 scade_si_atribuie y_minus_y,centruY, y_pornire
					
				 aduna_si_atribuie x_plus_y, centruX, y_pornire
				 scade_si_atribuie x_minus_y, centruX, y_pornire
				 aduna_si_atribuie y_plus_x, centruY,  x_pornire
				 scade_si_atribuie y_minus_x, centruY,  x_pornire
				
	   pune_pixel x_plus_x, y_plus_y
	   pune_pixel x_minus_x, y_plus_y
	   pune_pixel x_plus_y, y_minus_x
	   pune_pixel x_minus_y, y_minus_x
	   pune_pixel x_plus_x, y_minus_y	
	   pune_pixel x_minus_x, y_minus_y
	   pune_pixel x_minus_y, y_plus_x
	   pune_pixel x_plus_y, y_plus_x
		   aduna_3_si_atribuie d, d, x_pornire, x_pornire
		   compara_variabila_si_numarul d, 0
		   jl sens_negativ
		   decrementeaza y_pornire
		   scade_3_si_atribuie d, d, y_pornire, y_pornire
    
			sens_negativ:
			incrementeaza x_pornire
		
			compara x_pornire, y_pornire
			jg end_desenare
			jmp desenare_cerc_loop
    
    
    end_desenare:    
		atribuie x_pornire, 0
		atribuie y_pornire, 0 
    
		atribuie x_plus_x, 0
		atribuie x_minus_x, 0
		atribuie y_plus_y, 0
		atribuie y_minus_y, 0
    
		atribuie x_plus_y, 0
		atribuie x_minus_y, 0
		atribuie y_plus_x, 0
		atribuie y_minus_x, 0
		atribuie d, 1 ;parametru de decizie a urmatorului pixel ce va fi setat
		
		popa
endm


DeseneazaDreptunghi macro x1,y1,x2,y2
		local catre_dreapta,catre_stanga,fa_linie_orizontala_stanga,fa_linie_orizontala_dreapta,fa_linie_verticala_sus_s,in_sus,in_jos,end_desenare
		pusha
		compara x1, x2
		jg catre_stanga
		jl catre_dreapta
			catre_stanga:
				mov eax,x1
				sub eax,x2
				mov ecx, eax
				mov ebx, x1
				mov poz,ebx 
				fa_linie_orizontala_stanga:
				pune_pixel y1,poz
				pune_pixel y2,poz
				decrementeaza poz
				loop fa_linie_orizontala_stanga
		compara y1,y2
		ja in_sus
		jb in_jos
			in_sus:
				mov eax, y1
				sub eax, y2
				mov ecx, eax
				mov ebx, y1
				mov poz, ebx
				fa_linie_verticala_sus_s:
				pune_pixel poz,x2
				pune_pixel poz,x1
				decrementeaza poz
				loop fa_linie_verticala_sus_s
				jmp end_desenare
			in_jos:
					mov eax, y2
					sub eax, y1
					mov ecx, eax
					mov ebx, y2
					mov poz, ebx
					fa_linie_verticala_jos_s:
					pune_pixel poz,x2
					pune_pixel poz,x1
					decrementeaza poz
					loop fa_linie_verticala_jos_s
					jmp end_desenare
			catre_dreapta:
				mov eax,x2
				sub eax, x1
				mov ecx, eax
				mov ebx, x1
				mov poz, ebx
				fa_linie_orizontala_dreapta:
				pune_pixel y1,poz
				pune_pixel y2,poz
				incrementeaza poz
				loop fa_linie_orizontala_dreapta
		compara y1,y2
		ja in_sus
		jb in_jos
			
			
		end_desenare:
		popa
		endm	
		
		DeseneazaLinie macro x,y,x2,y2 
		local
		pusha
			scade_si_atribuie w,x2,x
			scade_si_atribuie h, y2,y
			atribuie dx1,0
			atribuie dy1,0
			atribuie dx2,0
			atribuie dy2,0
			compara w,0
			jl decide_w1
			jg decide_w2
		etch1:
			compara h,0
			jl decide_h1
			jg decide_h2
		etch2:
		compara w,0
			jl decide_ww1
			jg decide_ww2
			
		
		decide_w1:
				atribuie dx1, -1
				jmp etch1
		decide_w2:
				atribuie dx1, 1
				jmp etch1
		decide_h1:
				atribuie dy1, -1
				jmp etch2
		decide_h2:
				atribuie dy1, 1
				jmp etch2
		decide_ww1:
				atribuie dx2, -1
				jmp etch3
		decide_ww2:
				atribuie dx2, 1
				jmp etch3
				
		etch3:
			compara w,0
			jl negativ_w
			jge pozitiv_w
			
			pozitiv_w:
				atribuie longest, w
				jmp mai_departe_1
			negativ_w:
				mov eax,w
				mov ebx, -1
				mul ebx
				mov longest, eax
				jmp mai_departe_1		
		mai_departe_1:		
			compara h,0
			jl negativ_h
			jge pozitiv_h
			
			pozitiv_h:
				atribuie shortest, h
				jmp mai_departe_2
			negativ_h:
				mov eax, h
				mov ebx, -1
				mul ebx
				mov shortest, eax
				jmp mai_departe_2
		mai_departe_2:		
		compara longest, shortest
		jle if_1
		jg mai_departe_3;afara din if-ul mare
			if_1:
				compara h,0
			jl negativ_h_2
			jge pozitiv_h_2
			
			pozitiv_h_2:
				atribuie longest, h
				jmp mai_departe_3_0
			negativ_h_2:
				mov eax,h
				mov ebx, -1
				mul ebx
				mov longest, eax
				jmp mai_departe_3_0	
			mai_departe_3_0:			;al doilea lucru din if-ul mare
			;shortest = abs(w)
			compara w,0
			jl negativ_w_2
			jge pozitiv_w_2
			
			pozitiv_w_2:
				atribuie shortest, w
				jmp mai_departe_3_1
			negativ_w_2:
				mov eax,w
				mov ebx, -1
				mul ebx
				mov shortest, eax
				jmp mai_departe_3_1		
			
			mai_departe_3_1:		;al treilea lucru din if-ul mare
			compara h, 0
				jl mai_departe_3_1_0
				jg mai_departe_3_1_1
				
				mai_departe_3_1_0:
					atribuie dy2, -1
					jmp ultima_if_1
				mai_departe_3_1_1:
					atribuie dy2, 1
					jmp ultima_if_1
		ultima_if_1:
		atribuie dx2, 0

		mai_departe_3:
			mov eax, longest
			sar eax, 1
			mov numarator, eax
			;for
			incrementeaza longest
			mov ecx, longest
			forr:
			pune_pixel y, x
			aduna_si_atribuie numarator,numarator,shortest
			compara numarator,longest
				jge modifica
				jl nu_modifica
				
				modifica:
					scade_si_atribuie numarator, numarator, longest
					aduna_si_atribuie x, x, dx1
					aduna_si_atribuie y, y, dy1
					jmp final
				
				nu_modifica:
					aduna_si_atribuie x, x, dx2
					aduna_si_atribuie y, y, dy2
			final:		
			dec ecx
			jne forr
		popa
		endm
		
		

; functia de desenare - se apeleaza la fiecare click
; sau la fiecare interval de 200ms in care nu s-a dat click
; arg1 - evt (0 - initializare, 1 - click, 2 - s-a scurs intervalul fara click)
; arg2 - x
; arg3 - y
draw proc
	push ebp
	mov ebp, esp
	pusha
	
	mov eax, [ebp+arg1]
	cmp eax, 1
	jz evt_click
	cmp eax, 2
	jz evt_timer ; nu s-a efectuat click pe nimic
	;mai jos e codul care intializeaza fereastra cu pixeli albi
	mov eax, area_width
	mov ebx, area_height
	mul ebx
	shl eax, 2
	push eax
	push 255
	push area
	call memset
	add esp, 12
	
	;fundal albastru
	;pusha
	;mov eax, area_height
	;mov ecx, area_width
	;mul ecx
	;mov ecx, eax
	;mov edi, area
	;fundal_:
	;mov eax, ecx
	;shl eax, 2
	;add eax, edi
	;mov dword ptr[eax], 00037FFh
	;loop fundal_
	;popa
	
	
	;jmp final_draw
jmp evt_timer	
evt_click:
	
	mov eax,[ebp+arg3]  ;arg3=y-inaltime
	mov edi,[ebp+arg2]   ;latime(x)
	;verificam al catelea click este acesta pentru ca avem conditii diferite de pozitie
	;cmp clickx, 0
	;jne verifica_locatie_centru
	cmp contor, 1
	je verifica_locatie_selectie 	;este primul click
	cmp contor, 2          
	je verifica_locatie_click_1		;este click-ul pentru centru
	jne verifica_locatie_click_2		;este click-ul pentru lungimea razei
	
	verifica_locatie_selectie:
		;verificam unde este click-ul
		cmp eax, 100
		jg evt_timer ;mai mare decat bara orizontala
		;daca am ajuns aici inseamna ca e in meniu...vedem pe ce casuta
		cmp edi, 250
		jle este_dreapta
		cmp edi, 502
		jle este_dreptunghi
		cmp edi, 750
		jle este_cerc
		cmp edi, 860
		jle fa_clear
		cmp edi, 970
		jle fa_fundal_mov
		cmp edi, 1073
		jle fa_fundal_albastru
		cmp edi, 1186
		jle fa_fundal_galben
		jg evt_timer
		
		este_dreapta:
			mov dreapta, 1
			inc contor
			jmp evt_timer
		este_dreptunghi:
			mov dreptunghi, 1
			inc contor
			jmp evt_timer
		este_cerc:
			mov cerc, 1
			inc contor
			jmp evt_timer
			
		
		
		;am preluat ce functie trebuie sa aplicam
		
		verifica_locatie_click_1:
		;dec contor
		mov click_1_x, edi
		mov click_1_y, eax
		
		cmp click_1_y, 110
		jle evt_timer
		;cmp centru_y, 590
		;jge evt_timer
		inc contor		;daca am ajuns pana aici inseamna ca primul click de pe chenar a fost pus la o locatie valida
		jmp evt_timer
		
		verifica_locatie_click_2:
		mov click_2_x, edi
		mov click_2_y, eax
		;nu avem voie sa punem click-ul doi pe meniu
		cmp click_2_y, 110
		jle evt_timer
		;verificam ce functie avem de executat:
		cmp dreapta, 1
		je fa_dreapta
		cmp dreptunghi, 1
		je fa_dreptunghi
		;nu a fost pe dreptunghi
		cmp cerc, 1
		je fa_cerc
		
		fa_dreapta:
		compara click_1_X,click_2_x
			je evt_timer
		DeseneazaLinie click_1_x,click_1_y,click_2_x,click_2_y
		
			mov dreapta, 0
			dec contor
			dec contor
			jmp evt_timer
		fa_dreptunghi:
			compara click_1_X,click_2_x
			je evt_timer
			DeseneazaDreptunghi click_1_x,click_1_y,click_2_x,click_2_y
		
			mov dreptunghi, 0
			dec contor
			dec contor
			jmp evt_timer
		
		fa_cerc:	
		distanta click_2_x,click_2_y,click_1_x,click_1_y
		push ebx
		mov ebx, click_1_y
		sub ebx, 100
		mov aux3, ebx
		compara raza, aux3
		pop ebx
		ja evt_timer
		
		push ebx
		mov ebx, 680
		sub ebx, click_1_y
		mov aux3, ebx
		compara raza, aux3
		pop ebx
		ja evt_timer	
		
		compara click_1_x,click_2_x
		je evt_timer
		DeseneazaCerc click_1_y,click_1_x,raza
		mov cerc, 0
		dec contor
		dec contor
		jmp evt_timer
		
		fa_clear:
			pusha
			mov eax, area_height
			mov ecx, area_width
			mul ecx
			mov ecx, eax
			mov edi, area
			fundal__:
			mov eax, ecx
			shl eax, 2
			add eax, edi
			mov ebx, curent_color
			mov dword ptr[eax], ebx
			loop fundal__
			popa
			jmp evt_timer
		fa_fundal_mov:
			pusha
			atribuie curent_color, movv
			mov eax, area_height
			mov ecx, area_width
			mul ecx
			mov ecx, eax
			mov edi, area
			fundal___:
			mov eax, ecx
			shl eax, 2
			add eax, edi
			mov ebx, movv
			cmp dword ptr[eax], 0
			je asta_nu_m
			mov dword ptr[eax], ebx
			asta_nu_m:
			loop fundal___
			popa
			jmp evt_timer
		
		fa_fundal_albastru:
			pusha
			atribuie curent_color, albastru
			mov eax, area_height
			mov ecx, area_width
			mul ecx
			mov ecx, eax
			mov edi, area
			fundal____:
			mov eax, ecx
			shl eax, 2
			add eax, edi
			mov ebx, albastru
			cmp dword ptr[eax], 0
			je asta_nu_a
			mov dword ptr[eax], ebx
			asta_nu_a:
			loop fundal____
			popa
			jmp evt_timer
			
		fa_fundal_galben:
			pusha
			atribuie curent_color, galben
			mov eax, area_height
			mov ecx, area_width
			mul ecx
			mov ecx, eax
			mov edi, area
			fundal_____:
			mov eax, ecx
			shl eax, 2
			add eax, edi
			mov ebx, galben
			cmp dword ptr[eax], 0
			je asta_nu_g
			mov dword ptr[eax], ebx
			asta_nu_g:
			loop fundal_____
			popa
evt_timer:
	;CADRAN
	
	deseneaza_linie_orizontala 100,0
	deseneaza_linie_orizontala 101,0
	deseneaza_linie_orizontala 102,0
	deseneaza_linie_verticala 0, 250
	deseneaza_linie_verticala 0, 251
	deseneaza_linie_verticala 0, 252
	deseneaza_linie_verticala 0, 500
	deseneaza_linie_verticala 0, 501
	deseneaza_linie_verticala 0, 502
	deseneaza_linie_verticala 0, 750
	deseneaza_linie_verticala 0, 751
	deseneaza_linie_verticala 0, 752
	deseneaza_linie_verticala 0, 860
	deseneaza_linie_verticala 0, 861
	deseneaza_linie_verticala 0, 862
	deseneaza_linie_verticala 0, 970
	deseneaza_linie_verticala 0, 971
	deseneaza_linie_verticala 0, 972
	deseneaza_linie_verticala 0, 1073
	deseneaza_linie_verticala 0, 1074
	deseneaza_linie_verticala 0, 1075
	deseneaza_linie_verticala 0, 1186
	deseneaza_linie_verticala 0, 1187
	deseneaza_linie_verticala 0, 1188
	;prima casuta-dreapta
	deseneaza_linie_orizontala2 50,50
	deseneaza_linie_orizontala2 51,50
	;a doua casuta-dreptungi
	deseneaza_linie_orizontala3 83,289
	deseneaza_linie_orizontala3 84,289
	deseneaza_linie_verticala2 19,288
	deseneaza_linie_verticala2 19,287
	deseneaza_linie_orizontala3 20,289
	deseneaza_linie_orizontala3 21,289
	deseneaza_linie_verticala2 19,464
	deseneaza_linie_verticala2 19,463
	;a treia casuta-cerc
	
	DeseneazaCerc 50,632,45
	
	;a patra casuta-functia CLEAR
	make_text_macro 'C', area, 780, 40,curent_color
	make_text_macro 'L', area, 790, 40,curent_color
	make_text_macro 'E', area, 800, 40,curent_color
	make_text_macro 'A', area, 810, 40,curent_color
	make_text_macro 'R', area, 820, 40,curent_color
	;a cincea casuta-culoare_1
	make_text_macro ' ', area, 973, 1,albastru
	make_text_macro ' ', area, 983, 1,albastru
	make_text_macro ' ', area, 993, 1,albastru
	make_text_macro ' ', area, 1003, 1,albastru
	make_text_macro ' ', area, 1013, 1,albastru
	make_text_macro ' ', area, 1023, 1,albastru
	make_text_macro ' ', area, 1033, 1,albastru
	make_text_macro ' ', area, 1043, 1,albastru
	make_text_macro ' ', area, 1053, 1,albastru
	make_text_macro ' ', area, 1063, 1,albastru
	make_text_macro ' ', area, 973, 21,albastru
	make_text_macro ' ', area, 983, 21,albastru
	make_text_macro ' ', area, 993, 21,albastru
	make_text_macro ' ', area, 1003, 21,albastru
	make_text_macro ' ', area, 1013, 21,albastru
	make_text_macro ' ', area, 1023, 21,albastru
	make_text_macro ' ', area, 1033, 21,albastru
	make_text_macro ' ', area, 1043, 21,albastru
	make_text_macro ' ', area, 1053, 21,albastru
	make_text_macro ' ', area, 1063, 21,albastru
	make_text_macro ' ', area, 973, 31,albastru
	make_text_macro ' ', area, 983, 31,albastru
	make_text_macro ' ', area, 993, 31,albastru
	make_text_macro ' ', area, 1003, 31,albastru
	make_text_macro ' ', area, 1013, 31,albastru
	make_text_macro ' ', area, 1023, 31,albastru
	make_text_macro ' ', area, 1033, 31,albastru
	make_text_macro ' ', area, 1043, 31,albastru
	make_text_macro ' ', area, 1053, 31,albastru
	make_text_macro ' ', area, 1063, 31,albastru
	make_text_macro ' ', area, 973, 41,albastru
	make_text_macro ' ', area, 983, 41,albastru
	make_text_macro ' ', area, 993, 41,albastru
	make_text_macro ' ', area, 1003, 41,albastru
	make_text_macro ' ', area, 1013, 41,albastru
	make_text_macro ' ', area, 1023, 41,albastru
	make_text_macro ' ', area, 1033, 41,albastru
	make_text_macro ' ', area, 1043, 41,albastru
	make_text_macro ' ', area, 1053, 41,albastru
	make_text_macro ' ', area, 1063, 41,albastru
	make_text_macro ' ', area, 973, 51,albastru
	make_text_macro ' ', area, 983, 51,albastru
	make_text_macro ' ', area, 993, 51,albastru
	make_text_macro ' ', area, 1003, 51,albastru
	make_text_macro ' ', area, 1013, 51,albastru
	make_text_macro ' ', area, 1023, 51,albastru
	make_text_macro ' ', area, 1033, 51,albastru
	make_text_macro ' ', area, 1043, 51,albastru
	make_text_macro ' ', area, 1053, 51,albastru
	make_text_macro ' ', area, 1063, 51,albastru
	make_text_macro ' ', area, 973, 61,albastru
	make_text_macro ' ', area, 983, 61,albastru
	make_text_macro ' ', area, 993, 61,albastru
	make_text_macro ' ', area, 1003, 61,albastru
	make_text_macro ' ', area, 1013, 61,albastru
	make_text_macro ' ', area, 1023, 61,albastru
	make_text_macro ' ', area, 1033, 61,albastru
	make_text_macro ' ', area, 1043, 61,albastru
	make_text_macro ' ', area, 1053, 61,albastru
	make_text_macro ' ', area, 1063, 61,albastru
	make_text_macro ' ', area, 973, 71,albastru
	make_text_macro ' ', area, 983, 71,albastru
	make_text_macro ' ', area, 993, 71,albastru
	make_text_macro ' ', area, 1003, 71,albastru
	make_text_macro ' ', area, 1013, 71,albastru
	make_text_macro ' ', area, 1023, 71,albastru
	make_text_macro ' ', area, 1033, 71,albastru
	make_text_macro ' ', area, 1043, 71,albastru
	make_text_macro ' ', area, 1053, 71,albastru
	make_text_macro ' ', area, 1063, 71,albastru
	make_text_macro ' ', area, 973, 80,albastru
	make_text_macro ' ', area, 983, 80,albastru
	make_text_macro ' ', area, 993, 80,albastru
	make_text_macro ' ', area, 1003, 80,albastru
	make_text_macro ' ', area, 1013, 80,albastru
	make_text_macro ' ', area, 1023, 80,albastru
	make_text_macro ' ', area, 1033, 80,albastru
	make_text_macro ' ', area, 1043, 80,albastru
	make_text_macro ' ', area, 1053, 80,albastru
	make_text_macro ' ', area, 1063, 80,albastru
	;a sasea casuta-culoare 2
	make_text_macro ' ', area, 863, 1,movv
	make_text_macro ' ', area, 873, 1,movv
	make_text_macro ' ', area, 883, 1,movv
	make_text_macro ' ', area, 893, 1,movv
	make_text_macro ' ', area, 903, 1,movv
	make_text_macro ' ', area, 913, 1,movv
	make_text_macro ' ', area, 923, 1,movv
	make_text_macro ' ', area, 933, 1,movv
	make_text_macro ' ', area, 943, 1,movv
	make_text_macro ' ', area, 953, 1,movv
	make_text_macro ' ', area, 960, 1,movv
	make_text_macro ' ', area, 863, 11,movv
	make_text_macro ' ', area, 873, 11,movv
	make_text_macro ' ', area, 883, 11,movv
	make_text_macro ' ', area, 893, 11,movv
	make_text_macro ' ', area, 903, 11,movv
	make_text_macro ' ', area, 913, 11,movv
	make_text_macro ' ', area, 923, 11,movv
	make_text_macro ' ', area, 933, 11,movv
	make_text_macro ' ', area, 943, 11,movv
	make_text_macro ' ', area, 953, 11,movv
	make_text_macro ' ', area, 960, 11,movv
	make_text_macro ' ', area, 863, 21,movv
	make_text_macro ' ', area, 873, 21,movv
	make_text_macro ' ', area, 883, 21,movv
	make_text_macro ' ', area, 893, 21,movv
	make_text_macro ' ', area, 903, 21,movv
	make_text_macro ' ', area, 913, 21,movv
	make_text_macro ' ', area, 923, 21,movv
	make_text_macro ' ', area, 933, 21,movv
	make_text_macro ' ', area, 943, 21,movv
	make_text_macro ' ', area, 953, 21,movv
	make_text_macro ' ', area, 960, 21,movv
	make_text_macro ' ', area, 863, 31,movv
	make_text_macro ' ', area, 873, 31,movv
	make_text_macro ' ', area, 883, 31,movv
	make_text_macro ' ', area, 893, 31,movv
	make_text_macro ' ', area, 903, 31,movv
	make_text_macro ' ', area, 913, 31,movv
	make_text_macro ' ', area, 923, 31,movv
	make_text_macro ' ', area, 933, 31,movv
	make_text_macro ' ', area, 943, 31,movv
	make_text_macro ' ', area, 953, 31,movv
	make_text_macro ' ', area, 960, 31,movv
	make_text_macro ' ', area, 863, 41,movv
	make_text_macro ' ', area, 873, 41,movv
	make_text_macro ' ', area, 883, 41,movv
	make_text_macro ' ', area, 893, 41,movv
	make_text_macro ' ', area, 903, 41,movv
	make_text_macro ' ', area, 913, 41,movv
	make_text_macro ' ', area, 923, 41,movv
	make_text_macro ' ', area, 933, 41,movv
	make_text_macro ' ', area, 943, 41,movv
	make_text_macro ' ', area, 953, 41,movv
	make_text_macro ' ', area, 960, 41,movv
	make_text_macro ' ', area, 863, 51,movv
	make_text_macro ' ', area, 873, 51,movv
	make_text_macro ' ', area, 883, 51,movv
	make_text_macro ' ', area, 893, 51,movv
	make_text_macro ' ', area, 903, 51,movv
	make_text_macro ' ', area, 913, 51,movv
	make_text_macro ' ', area, 923, 51,movv
	make_text_macro ' ', area, 933, 51,movv
	make_text_macro ' ', area, 943, 51,movv
	make_text_macro ' ', area, 953, 51,movv
	make_text_macro ' ', area, 960, 51,movv
	make_text_macro ' ', area, 863, 61,movv
	make_text_macro ' ', area, 873, 61,movv
	make_text_macro ' ', area, 883, 61,movv
	make_text_macro ' ', area, 893, 61,movv
	make_text_macro ' ', area, 903, 61,movv
	make_text_macro ' ', area, 913, 61,movv
	make_text_macro ' ', area, 923, 61,movv
	make_text_macro ' ', area, 933, 61,movv
	make_text_macro ' ', area, 943, 61,movv
	make_text_macro ' ', area, 953, 61,movv
	make_text_macro ' ', area, 960, 61,movv
	make_text_macro ' ', area, 863, 80,movv
	make_text_macro ' ', area, 873, 80,movv
	make_text_macro ' ', area, 883, 80,movv
	make_text_macro ' ', area, 893, 80,movv
	make_text_macro ' ', area, 903, 80,movv
	make_text_macro ' ', area, 913, 80,movv
	make_text_macro ' ', area, 923, 80,movv
	make_text_macro ' ', area, 933, 80,movv
	make_text_macro ' ', area, 943, 80,movv
	make_text_macro ' ', area, 953, 80,movv
	make_text_macro ' ', area, 960, 80,movv
	;a saptea casuta-culoare_3
	make_text_macro ' ', area, 1076, 1,galben
	make_text_macro ' ', area, 1086, 1,galben
	make_text_macro ' ', area, 1096, 1,galben
	make_text_macro ' ', area, 1106, 1,galben
	make_text_macro ' ', area, 1116, 1,galben
	make_text_macro ' ', area, 1126, 1,galben
	make_text_macro ' ', area, 1136, 1,galben
	make_text_macro ' ', area, 1146, 1,galben
	make_text_macro ' ', area, 1156, 1,galben
	make_text_macro ' ', area, 1166, 1,galben
	make_text_macro ' ', area, 1176, 1,galben
	make_text_macro ' ', area, 1076, 11,galben
	make_text_macro ' ', area, 1086, 11,galben
	make_text_macro ' ', area, 1096, 11,galben
	make_text_macro ' ', area, 1106, 11,galben
	make_text_macro ' ', area, 1116, 11,galben
	make_text_macro ' ', area, 1126, 11,galben
	make_text_macro ' ', area, 1136, 11,galben
	make_text_macro ' ', area, 1146, 11,galben
	make_text_macro ' ', area, 1156, 11,galben
	make_text_macro ' ', area, 1166, 11,galben
	make_text_macro ' ', area, 1176, 11,galben
	make_text_macro ' ', area, 1076, 21,galben
	make_text_macro ' ', area, 1086, 21,galben
	make_text_macro ' ', area, 1096, 21,galben
	make_text_macro ' ', area, 1106, 21,galben
	make_text_macro ' ', area, 1116, 21,galben
	make_text_macro ' ', area, 1126, 21,galben
	make_text_macro ' ', area, 1136, 21,galben
	make_text_macro ' ', area, 1146, 21,galben
	make_text_macro ' ', area, 1156, 21,galben
	make_text_macro ' ', area, 1166, 21,galben
	make_text_macro ' ', area, 1176, 21,galben
	make_text_macro ' ', area, 1076, 31,galben
	make_text_macro ' ', area, 1086, 31,galben
	make_text_macro ' ', area, 1096, 31,galben
	make_text_macro ' ', area, 1106, 31,galben
	make_text_macro ' ', area, 1116, 31,galben
	make_text_macro ' ', area, 1126, 31,galben
	make_text_macro ' ', area, 1136, 31,galben
	make_text_macro ' ', area, 1146, 31,galben
	make_text_macro ' ', area, 1156, 31,galben
	make_text_macro ' ', area, 1166, 31,galben
	make_text_macro ' ', area, 1176, 31,galben
	make_text_macro ' ', area, 1076, 41,galben
	make_text_macro ' ', area, 1086, 41,galben
	make_text_macro ' ', area, 1096, 41,galben
	make_text_macro ' ', area, 1106, 41,galben
	make_text_macro ' ', area, 1116, 41,galben
	make_text_macro ' ', area, 1126, 41,galben
	make_text_macro ' ', area, 1136, 41,galben
	make_text_macro ' ', area, 1146, 41,galben
	make_text_macro ' ', area, 1156, 41,galben
	make_text_macro ' ', area, 1166, 41,galben
	make_text_macro ' ', area, 1176, 41,galben
	make_text_macro ' ', area, 1076, 51,galben
	make_text_macro ' ', area, 1086, 51,galben
	make_text_macro ' ', area, 1096, 51,galben
	make_text_macro ' ', area, 1106, 51,galben
	make_text_macro ' ', area, 1116, 51,galben
	make_text_macro ' ', area, 1126, 51,galben
	make_text_macro ' ', area, 1136, 51,galben
	make_text_macro ' ', area, 1146, 51,galben
	make_text_macro ' ', area, 1156, 51,galben
	make_text_macro ' ', area, 1166, 51,galben
	make_text_macro ' ', area, 1176, 51,galben
	make_text_macro ' ', area, 1076, 61,galben
	make_text_macro ' ', area, 1086, 61,galben
	make_text_macro ' ', area, 1096, 61,galben
	make_text_macro ' ', area, 1106, 61,galben
	make_text_macro ' ', area, 1116, 61,galben
	make_text_macro ' ', area, 1126, 61,galben
	make_text_macro ' ', area, 1136, 61,galben
	make_text_macro ' ', area, 1146, 61,galben
	make_text_macro ' ', area, 1156, 61,galben
	make_text_macro ' ', area, 1166, 61,galben
	make_text_macro ' ', area, 1176, 61,galben
	make_text_macro ' ', area, 1076, 71,galben
	make_text_macro ' ', area, 1086, 71,galben
	make_text_macro ' ', area, 1096, 71,galben
	make_text_macro ' ', area, 1106, 71,galben
	make_text_macro ' ', area, 1116, 71,galben
	make_text_macro ' ', area, 1126, 71,galben
	make_text_macro ' ', area, 1136, 71,galben
	make_text_macro ' ', area, 1146, 71,galben
	make_text_macro ' ', area, 1156, 71,galben
	make_text_macro ' ', area, 1166, 71,galben
	make_text_macro ' ', area, 1176, 71,galben
	make_text_macro ' ', area, 1076, 80,galben
	make_text_macro ' ', area, 1086, 80,galben
	make_text_macro ' ', area, 1096, 80,galben
	make_text_macro ' ', area, 1106, 80,galben
	make_text_macro ' ', area, 1116, 80,galben
	make_text_macro ' ', area, 1126, 80,galben
	make_text_macro ' ', area, 1136, 80,galben
	make_text_macro ' ', area, 1146, 80,galben
	make_text_macro ' ', area, 1156, 80,galben
	make_text_macro ' ', area, 1166, 80,galben
	make_text_macro ' ', area, 1176, 80,galben
final_draw:

	popa
	mov esp, ebp
	pop ebp
	ret
draw endp

start:
	;alocam memorie pentru zona de desenat
	mov eax, area_width
	mov ebx, area_height
	mul ebx
	shl eax, 2
	push eax
	call malloc
	add esp, 4
	mov area, eax
	;apelam functia de desenare a ferestrei
	; typedef void (*DrawFunc)(int evt, int x, int y);
	; void __cdecl BeginDrawing(const char *title, int width, int height, unsigned int *area, DrawFunc draw);
	push offset draw
	push area
	push area_height
	push area_width
	push offset window_title
	call BeginDrawing
	add esp, 20
	
	;terminarea programului
	push 0
	call exit
end start
