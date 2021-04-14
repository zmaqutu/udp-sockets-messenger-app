# binary search program makefile
# Zongo Maqutu
# March 2020

JAVAC=/usr/bin/javac

.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<


#CLASSES=SequentialBasins.class ParallelThreads.class ParallelBasins.class

#CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

#default: $(CLASS_FILES)

all: 
	@javac -d bin $(SRCDIR)/*.java
clean:
	rm $(BINDIR)/*.class
	rm $(SRCDIR)/*.class

#to run the makefile with arguments use the command 'make SequentialBasins ARGS="YOUR ARGUMENTS"'or ParallelBasins ARGS = "YOUR ARGUMENTS"

runClient:
	@java -cp bin clientDriver

runServer:
	@java -cp bin serverDriver


#Write a section that will generate and clean Javadoc 

docs:
	@javadoc -d docs/ src/*.java
cleandocs:
	@rm -r docs/

