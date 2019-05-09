/* TEMPLATE GENERATED TESTCASE FILE
Filename: CWE15_External_Control_of_System_or_Configuration_Setting__basic_01.c
Label Definition File: CWE15_External_Control_of_System_or_Configuration_Setting__basic.label.xml
Template File: point-flaw-01.tmpl.c
*/
/*
 * @description
 * CWE: 15 External Control of System or Configuration Setting
 * Sinks:
 *    GoodSink: Properly authenticate all requests to set the hostname
 *    BadSink : Set the hostname to data obtained from the network / external source
 * Flow Variant: 01 Baseline
 *
 * @status Reviewed
 *
 * @history

2010-07-28 XXXXX.XXXXX@XXXXX reviewed
2010-07-22 XXXXX@XXXXX renamed variant in file name to basic
2010-07-20 XXXXX@XXXXX initial review for QC; changed status to "Ready for Review"
2010-06-09 XXXXX@XXXXX initial version converted from baseline

 */

#include "std_testcase.h"

# include <winsock2.h>
# pragma comment(lib, "ws2_32")

#ifdef _WIN32
# define CLOSESOCKET closesocket
# define UNLINK _unlink
#endif

#define LISTEN_PORT 999
#define LISTEN_BACKLOG 5
#define HOSTNAME_SZ 15
#define PASSWORD_SZ 20
#define PASSWORD "WGXGSYLNKFVBIUZUWNHL" /* maintenance note: ensure this is == PASSWORD_SZ */ /* INCIDENTAL CWE 259 - Hardcoded Password */

#ifndef OMITBAD

void CWE15_External_Control_of_System_or_Configuration_Setting__basic_01_bad()
{
    {
#ifdef _WIN32
        WSADATA wsadata;
        BOOL wsa_init = FALSE;
        SOCKET listener = INVALID_SOCKET;
        SOCKET client = INVALID_SOCKET;
        struct sockaddr_in s_in;
        char hostname[HOSTNAME_SZ+1];
        do
        {
            if (0 != WSAStartup(MAKEWORD(2, 2), &wsadata)) break;
            wsa_init = TRUE;
            listener = socket(PF_INET, SOCK_STREAM, 0);
            if (listener == INVALID_SOCKET) break;
            memset(&s_in, 0, sizeof(s_in));
            s_in.sin_family = AF_INET;
            s_in.sin_addr.s_addr = INADDR_ANY;
            s_in.sin_port = htons(LISTEN_PORT);
            if (SOCKET_ERROR == bind(listener, (struct sockaddr*)&s_in, sizeof(s_in))) break;
            if (SOCKET_ERROR == listen(listener, LISTEN_BACKLOG)) break;
            client = accept(listener, NULL, NULL);
            if (client == INVALID_SOCKET) break;
            /* INCIDENTAL CWE 188 - reliance on data memory layout
             * recv and friends return "number of bytes" received
             * char's on our system, however, may not be "octets" (8-bit
             * bytes) but could be just about anything.  Also,
             * even if the external environment is ASCII or UTF8,
             * the ANSI/ISO C standard does not dictate that the
             * character set used by the actual language or character
             * constants matches.
             *
             * In practice none of these are usually issues...
             */
            if (sizeof(hostname)-sizeof(char) != recv(client, hostname, sizeof(hostname)-sizeof(char), 0)) break;
            hostname[HOSTNAME_SZ] = '\0';
            /* FLAW: set the hostname to data obtained from the network / external source */
            SetComputerNameA(hostname);
        }
        while (0);
        if (client != INVALID_SOCKET) CLOSESOCKET(client);
        if (listener != INVALID_SOCKET) CLOSESOCKET(listener);
        if (wsa_init) WSACleanup();
#endif /* _WIN32 */
    }
}

#endif /* OMITBAD */

#ifndef OMITGOOD

static void good1()
{
    {
#ifdef _WIN32
        WSADATA wsadata;
        BOOL wsa_init = FALSE;
        SOCKET listener = INVALID_SOCKET;
        SOCKET client = INVALID_SOCKET;
        struct sockaddr_in s_in;
        char hostname[HOSTNAME_SZ+1];
        char password[PASSWORD_SZ+1];
        do
        {
            if (0 != WSAStartup(MAKEWORD(2, 2), &wsadata)) break;
            wsa_init = TRUE;
            listener = socket(PF_INET, SOCK_STREAM, 0);
            if (listener == INVALID_SOCKET) break;
            memset(&s_in, 0, sizeof(s_in));
            s_in.sin_family = AF_INET;
            s_in.sin_addr.s_addr = INADDR_ANY;
            s_in.sin_port = htons(LISTEN_PORT);
            if (SOCKET_ERROR == bind(listener, (struct sockaddr*)&s_in, sizeof(s_in))) break;
            if (SOCKET_ERROR == listen(listener, LISTEN_BACKLOG)) break;
            client = accept(listener, NULL, NULL);
            if (client == INVALID_SOCKET) break;
            /* INCIDENTAL CWE 188 - reliance on data memory layout
             * recv and friends return "number of bytes" received
             * char's on our system, however, may not be "octets" (8-bit
             * bytes) but could be just about anything.  Also,
             * even if the external environment is ASCII or UTF8,
             * the ANSI/ISO C standard does not dictate that the
             * character set used by the actual language or character
             * constants matches.
             *
             * In practice none of these are usually issues...
             */
            /* INCIDENTAL CWE 319 - Plaintext Transmission of Sensitive Information */
            /* Also, we only obtained a 'password' and not also the 'username'.  This
             * doesn't really matter too much since it basically requires admin access,
             * but it'd make better sense (for auditing / logging) if separates user
             * accounts were used.  Which brings up another potential CWE (maybe), that
             * this kind of operation is not logged at all
             */
            if (sizeof(password)-sizeof(char) != recv(client, password, sizeof(password)-sizeof(char), 0)) break;
            /* FIX: Properly authenticate all requests to set the hostname */
            if (memcmp(password, PASSWORD, sizeof(password)-sizeof(char)) != 0)
            {
                printLine("Access denied");
                break;
            }
            if (sizeof(hostname)-sizeof(char) != recv(client, hostname, sizeof(hostname)-sizeof(char), 0)) break;
            hostname[HOSTNAME_SZ] = '\0';
            SetComputerNameA(hostname);
        }
        while (0);
        if (client != INVALID_SOCKET) CLOSESOCKET(client);
        if (listener != INVALID_SOCKET) CLOSESOCKET(listener);
        if (wsa_init) WSACleanup();
#endif /* _WIN32 */
    }
}

void CWE15_External_Control_of_System_or_Configuration_Setting__basic_01_good()
{
    good1();
}

#endif /* OMITGOOD */

/* Below is the main(). It is only used when building this testcase on
   its own for testing or for building a binary to use in testing binary
   analysis tools. It is not used when compiling all the testcases as one
   application, which is how source code analysis tools are tested. */

#ifdef INCLUDEMAIN

int main(int argc, char * argv[])
{
    /* seed randomness */
    srand( (unsigned)time(NULL) );
#ifndef OMITGOOD
    printLine("Calling good()...");
    CWE15_External_Control_of_System_or_Configuration_Setting__basic_01_good();
    printLine("Finished good()");
#endif /* OMITGOOD */
#ifndef OMITBAD
    printLine("Calling bad()...");
    CWE15_External_Control_of_System_or_Configuration_Setting__basic_01_bad();
    printLine("Finished bad()");
#endif /* OMITBAD */
    return 0;
}

#endif
