#include <iostream>
#include "Test.h"
int main() {
	testAll();
	testAllExtins();
	_CrtDumpMemoryLeaks();
	printf("Succes teste!\n");
	system("pause");
	return 0;
}