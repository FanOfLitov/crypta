#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <time.h>

#define KEY_SIZE 32 // 256 бит = 32 байта
#define BLOCK_SIZE 16 // Размер блока для шифрования

void generate_key(const char *password, uint8_t *key);
void encrypt_block(uint8_t *block, const uint8_t *key);
void decrypt_block(uint8_t *block, const uint8_t *key);
void permute_step(uint8_t *block);
void substitute_step(uint8_t *block);
void inverse_permute_step(uint8_t *block);
void inverse_substitute_step(uint8_t *block);
void encrypt_file(const char *input_file, const char *output_file, const uint8_t *key);
void decrypt_file(const char *input_file, const char *output_file, const uint8_t *key);
void test_encryption();

// Генерация ключа из парольной фразы
void generate_key(const char *password, uint8_t *key) {
    // Простой пример: используем первые 32 байта парольной фразы как ключ
    memset(key, 0, KEY_SIZE);
    strncpy((char *)key, password, KEY_SIZE);
}

// Перестановочный шаг
void permute_step(uint8_t *block) {
    uint8_t temp[BLOCK_SIZE];
    for (int i = 0; i < BLOCK_SIZE; i++) {
        temp[i] = block[(i + 3) % BLOCK_SIZE]; // Пример простой перестановки
    }
    memcpy(block, temp, BLOCK_SIZE);
}

// Подстановочный шаг
void substitute_step(uint8_t *block) {
    for (int i = 0; i < BLOCK_SIZE; i++) {
        block[i] = (block[i] + 5) % 256; // Пример простой подстановки
    }
}

// Обратный перестановочный шаг
void inverse_permute_step(uint8_t *block) {
    uint8_t temp[BLOCK_SIZE];
    for (int i = 0; i < BLOCK_SIZE; i++) {
        temp[(i + 3) % BLOCK_SIZE] = block[i]; // Обратная перестановка
    }
    memcpy(block, temp, BLOCK_SIZE);
}

// Обратный подстановочный шаг
void inverse_substitute_step(uint8_t *block) {
    for (int i = 0; i < BLOCK_SIZE; i++) {
        block[i] = (block[i] - 5 + 256) % 256; // Обратная подстановка
    }
}


void encrypt_block(uint8_t *block, const uint8_t *key) {
    for (int i = 0; i < BLOCK_SIZE; i++) {
        block[i] ^= key[i % KEY_SIZE]; // XOR с ключом
    }
    permute_step(block);       // Первый перестановочный шаг
    substitute_step(block);    // Первый подстановочный шаг
    permute_step(block);       // Второй перестановочный шаг
    substitute_step(block);    // Второй подстановочный шаг
}


void decrypt_block(uint8_t *block, const uint8_t *key) {
    inverse_substitute_step(block);    // Обратный второй подстановочный шаг
    inverse_permute_step(block);       // Обратный второй перестановочный шаг
    inverse_substitute_step(block);    // Обратный первый подстановочный шаг
    inverse_permute_step(block);       // Обратный первый перестановочный шаг
    for (int i = 0; i < BLOCK_SIZE; i++) {
        block[i] ^= key[i % KEY_SIZE]; // XOR с ключом
    }
}


void encrypt_file(const char *input_file, const char *output_file, const uint8_t *key) {
    FILE *in = fopen(input_file, "rb");
    FILE *out = fopen(output_file, "wb");
    if (!in || !out) {
        perror("Ошибка открытия файла");
        return;
    }

    uint8_t buffer[BLOCK_SIZE];
    size_t bytes_read;

    while ((bytes_read = fread(buffer, 1, BLOCK_SIZE, in)) > 0) {
        if (bytes_read < BLOCK_SIZE) {
            memset(buffer + bytes_read, 0, BLOCK_SIZE - bytes_read); // Дополнение нулями
        }
        encrypt_block(buffer, key);
        fwrite(buffer, 1, BLOCK_SIZE, out);
    }

    fclose(in);
    fclose(out);
}


void decrypt_file(const char *input_file, const char *output_file, const uint8_t *key) {
    FILE *in = fopen(input_file, "rb");
    FILE *out = fopen(output_file, "wb");
    if (!in || !out) {
        perror("Ошибка открытия файла");
        return;
    }

    uint8_t buffer[BLOCK_SIZE];
    size_t bytes_read;

    while ((bytes_read = fread(buffer, 1, BLOCK_SIZE, in)) > 0) {
        decrypt_block(buffer, key);
        fwrite(buffer, 1, bytes_read, out);
    }

    fclose(in);
    fclose(out);
}


void test_encryption() {
    uint8_t key[KEY_SIZE];
    generate_key("test_password", key);

    const char *plaintext = "Hello, World!";
    const char *encrypted_file = "encrypted.bin";
    const char *decrypted_file = "decrypted.txt";

    
    FILE *original = fopen("original.txt", "w");
    fprintf(original, "%s", plaintext);
    fclose(original);

   
    encrypt_file("original.txt", encrypted_file, key);

    
    decrypt_file(encrypted_file, decrypted_file, key);

   
    FILE *decrypted = fopen(decrypted_file, "r");
    char result[100];
    fgets(result, sizeof(result), decrypted);
    fclose(decrypted);

    if (strcmp(result, plaintext) == 0) {
        printf("Тест успешно пройден!\n");
    } else {
        printf("Тест не пройден!\n");
    }
}

int main(int argc, char *argv[]) {
    if (argc < 4) {
        printf("Использование: %s [encrypt|decrypt|test] <input_file> <output_file>\n", argv[0]);
        return 1;
    }

    const char *mode = argv[1];
    const char *input_file = argv[2];
    const char *output_file = argv[3];

    uint8_t key[KEY_SIZE];
    generate_key("default_password", key);

    if (strcmp(mode, "encrypt") == 0) {
        encrypt_file(input_file, output_file, key);
        printf("Файл успешно зашифрован.\n");
    } else if (strcmp(mode, "decrypt") == 0) {
        decrypt_file(input_file, output_file, key);
        printf("Файл успешно дешифрован.\n");
    } else if (strcmp(mode, "test") == 0) {
        test_encryption();
    } else {
        printf("Неизвестный режим. Используйте 'encrypt', 'decrypt' или 'test'.\n");
    }

    return 0;
}
