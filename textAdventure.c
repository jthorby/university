/*
	* Date: 29/08/2016
	* Author: Justin Thorby
	
	Program that takes a world description for a retro text adventure
	game and allows the user to move through the world
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>

#define MAX_LINE_LENGTH 500
#define MAX_NODES 1000
#define MAX_EDGES 4000


typedef struct node Node;

struct node {
	bool visited;
    int number;
    char name[MAX_LINE_LENGTH];
    char description[MAX_LINE_LENGTH];
    Node *north;
    Node *south;
    Node *west;
    Node *east;
};


void printDesc(char *description)
{
	char descCopy[MAX_LINE_LENGTH];
    int current_col = 0;
    int max_col = 48;
	char *token = NULL;
    int length = 0;
    
    strncpy(descCopy, description, strlen(description) + 1);

    token = strtok(descCopy, " \n\t\v\f\r");

    while (token != NULL) {
        length = strlen(token);

        if ((current_col + length) <= max_col) {
            printf("%s ", token);
            current_col += length + 1;

        } else {
            puts("");
            printf("%s ", token);
            current_col = length + 1;
        }
        token = strtok(NULL, " \n\t\v\f\r");
    }
    puts("");
}


Node *processInput(char input, Node * current)
{
    if (input == 'N') {
        if ((current->north) == NULL) {
            puts("You can't go North");
        } else {
            printf("North to %s\n", current->north->name);
            current = current->north;
            
        }
    } else if (input == 'S') {
        if ((current->south) == NULL) {
            puts("You can't go South");
        } else {
            printf("South to %s\n", current->south->name);
            current = current->south;
        }
    } else if (input == 'E') {
        if ((current->east) == NULL) {
            puts("You can't go East");
        } else {
            printf("East to %s\n", current->east->name);
            current = current->east;
        }
    } else if (input == 'W') {
        if ((current->west) == NULL) {
            puts("You can't go West");
        } else {
            printf("West to %s\n", current->west->name);
            current = current->west;
        }
    } else if (input == 'Q') {
        puts("Bye!");
        exit(0);
    } else if ((input == 'L') && (current->description[0] != 0)) {
        printDesc(current->description);
    }

    return current;
}


void playGame(Node * current)
{
    char input = 0;
    int count = 0;
    char extra = '\0';

    printf("%s\n", current->name);
    printDesc(current->description);
    current->visited = true;
    
    while ((input = getchar()) != EOF) {
        count = 1;

        if (input != '\n') {    // ignore empty lines
            while ((extra = getchar()) != '\n') {       // ignore a line with more chars than 1
                count += 1;
            }

            if ((count == 1) && input != '\n') {
                input = toupper(input);
                current = processInput(input, current);
                
                if ((current->visited == false) && (current->description[0] != 0)) {
					printDesc(current->description);
					current->visited = true;
				}
            }
        }
    }
}


void insertEdge(int from, int goesTo, char direction, Node nodeList[])
{
    Node *next = &nodeList[goesTo];

    if (direction == 'N') {
        nodeList[from].north = next;
    } else if (direction == 'S') {
        nodeList[from].south = next;
    } else if (direction == 'E') {
        nodeList[from].east = next;
    } else if (direction == 'W') {
        nodeList[from].west = next;
    }
}


FILE *readLine1(FILE * inputFile, int *numNodesPointer,
                int *numEdgesPointer)
{
    char buffer[MAX_LINE_LENGTH] = { '\0' };

    fgets(buffer, MAX_LINE_LENGTH, inputFile);

    sscanf(buffer, "%d %d", numNodesPointer, numEdgesPointer);

    return inputFile;
}


FILE *readNodes(FILE * inputFile, Node * nodeList, int numNodes)
{
    char lineBuffer[MAX_LINE_LENGTH] = { '\0' };
    char *endOfName = NULL;
    char *desc = NULL;
    int i = 0;
    bool whiteSpace = NULL;

    for (i = 0; ((i < numNodes) && (i < MAX_NODES)); i += 1) {
        lineBuffer[0] = '\0';

        fgets(lineBuffer, MAX_LINE_LENGTH, inputFile);

        Node nodetoInsert;
        nodetoInsert.visited = false;
        nodetoInsert.number = i;
        nodetoInsert.north = NULL;
        nodetoInsert.south = NULL;
        nodetoInsert.west = NULL;
        nodetoInsert.east = NULL;

        endOfName = strchr(lineBuffer, ':');

        if (endOfName == NULL) {
            endOfName = strchr(lineBuffer, '\n');
        }

        desc = endOfName + 1;

        strncpy(nodetoInsert.name, lineBuffer,
                strlen(lineBuffer) - strlen(endOfName));
        nodetoInsert.name[strlen(lineBuffer) - strlen(endOfName)] = '\0';

        whiteSpace = true;
        for (int i = 0; desc[i] != '\0'; i += 1) {
            if (!(isspace(desc[i]))) {
                whiteSpace = false;
            }
        }

        if (whiteSpace == false) {
            strncpy(nodetoInsert.description, desc, strlen(desc) + 1);
            nodetoInsert.description[strlen(desc)] = '\0';
        } else {
            nodetoInsert.description[0] = '\0';
        }

        nodeList[i] = nodetoInsert;
    }

    Node lastNode;
    lastNode.number = i;
    lastNode.north = NULL;
    lastNode.south = NULL;
    lastNode.west = NULL;
    lastNode.east = NULL;

    nodeList[i] = lastNode;

    return inputFile;
}


FILE *readEdges(FILE * inputFile, int numEdges, Node * nodeList)
{
    int head = 0;
    int tail = 0;
    char headDir = 0;
    char tailDir = 0;
    char edgeBuffer[MAX_LINE_LENGTH] = { '\0' };

    for (int i = 0; ((i < numEdges) && (i < MAX_EDGES)); i += 1) {
        fgets(edgeBuffer, MAX_LINE_LENGTH, inputFile);

        sscanf(edgeBuffer, "%d %d %c %c", &head, &tail, &headDir,
               &tailDir);

        insertEdge(head, tail, headDir, nodeList);
        insertEdge(tail, head, tailDir, nodeList);
    }

    return inputFile;
}


Node *readFile(FILE * inputFile, Node * nodeList)
{
    int numNodes = 0;
    int numEdges = 0;
    Node *start = NULL;

    //-------------------Read the first line----------------------------
    inputFile = readLine1(inputFile, &numNodes, &numEdges);
    //==================================================================

    //-------------------Read the nodes---------------------------------
    nodeList = malloc((sizeof(Node) * (numNodes + 1)));

    inputFile = readNodes(inputFile, nodeList, numNodes);

    start = &nodeList[0];
    //==================================================================

    //---------------------Read the edges-------------------------------
    inputFile = readEdges(inputFile, numEdges, nodeList);
    //==================================================================

    return start;
}


int main(int argc, char *argv[])
{
    FILE *inputFile = fopen(argv[1], "r");
    Node *nodeList = NULL;

    if ((argc != 2) || (inputFile == NULL)) {
        puts("Missing or invalid world file.");
        exit(1);
    } else {
        Node *start = readFile(inputFile, nodeList);
        playGame(start);
    }

    return EXIT_SUCCESS;
}
