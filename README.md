# OpenDMA Java Tutorial

> This tutorial is copyright by xaldon Technologies GmbH / Stefan Kopf and licensed under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/).

So you decided to – or have been told to – get familiar with OpenDMA development in Java.
This tutorial will guide you through your first steps with the OpenDMA API and provide
enough basic knowledge so you can continue learning by yourself.

## Some fundamentals

OpenDMA organizes all information in so called “objects”. Each object consists of a set
of “properties” that contain the atomic data. A property is uniquely identify by its
qualified name and can contain data of one predefined type. The special value “null”
denotes an empty property. Property values can either be of single valued or
multi valued cardinality. A single valued property can contain exactly one value (or
null) while a multi valued property can contain a (potentially empty) set of values. In
addition to some basic data types like Booleans, Integers or Strings, there is a
“Reference” data type that contains a reference or pointer to another object. The special
data type “Content” provides a stream based access to binary data.

An OpenDMA “Repository” is a (potentially very large) set of related objects. References
between objects are only valid within the same Repository. Even the repository itself is
represented as OpenDMA object that is contained in itself.

But now, let’s turn this dry theory into life by getting our hands on some simple OpenDMA
applications.

## Setting up your environment

All tutorial applications are nicely packed into a maven project with all required
dependencies available on Maven central.  
Simply check out this repository and open it with your favorite Java IDE
like Eclipse or IntelliJ. You can run each tutorial app right from within your IDE.

## Session management

As many ECM systems are designed following a client/server architecture, OpenDMA uses a
connection based concept. At first, you need to establish an `OdmaSession` to an OpenDMA
capable system. These sessions are provided by `OdmaAdaptor`s. An
“Adaptor” is a piece of software that makes an ECM system available to OpenDMA and
performs the mapping of different concepts and data structures to OpenDMA. There are
Adaptors available for many popular ECM systems, for example for Alfresco, FileNet P8,
Documentum, Sharepoint, OpenText Content Services, ContentManager OnDemand CMOD, IBM DB2
ContentManager, FileNet ImageServices, Livelink, and more.

Adaptors are packaged as .jar files that need to be put on your classpath – very similar
to JDBC drivers:

The `OdmaAdaptorDiscovery` helper can find adaptors available on the classpath based on
their *system ID*:

```java
OdmaAdaptorDiscovery adaptorDiscovery = new OdmaAdaptorDiscovery();
OdmaAdaptor xmlRepoAdaptor = adaptorDiscovery.getAdaptor("xmlrepo");
```

Once you have the `OdmaAdaptor`, set up your connection parameters to connect to the
target system:

```java
Properties props = new Properties();
props.put("classpathResource", "SampleRepository.xml");
OdmaSession session = xmlRepoAdaptor.connect(props);
```

Connection parameters are specific to the adaptor you are using. In the case of the
`xmlrepo` adaptor, we only need the location of the XML file containing the object
descriptions. There are a couple of standardised parameter names available in the
`OdmaAdaptor` interface.

Connecting to other ECM systems is straight forward. For example, to connect to Alfresco:

```java
OdmaAdaptor alfAdaptor = adaptorDiscovery.getAdaptor("alfresco");
Properties params = new Properties();
params.setProperty(OdmaAdaptor.PARAM_ENDPOINT, "http://localhost:8080/alfresco/");
params.setProperty(OdmaAdaptor.PARAM_USERNAME, "admin");
params.setProperty(OdmaAdaptor.PARAM_PASSWORD, "admin");
OdmaSession alfSession = alfAdaptor.connect(params);
```
(this code requires an Alfresco OpenDMA adaptor on the classpath)

At the end, you always need to `close()` your session to release all occupied resources:
```java
// always close the session
session.close();
```

There are scenarios, like dependency injection and IoC containers, where you want to
separate connection parameters from the code establishing a session. OpenDMA
provides the concept of “session providers” to meet this need:

```java
// instantiate and configure OdmaSessionProvider
XmlRepositorySessionProvider sessionProvider = new XmlRepositorySessionProvider();
sessionProvider.setClasspathResource("SampleRepository.xml");
OdmaSession session = sessionProvider.getSession();
```

You can find the above code in [Lession1_ObtainASession](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession01_ObtainASession.java)

## Listing Repositories

With a session, you have a connection to an instance of an OpenDMA capable system. This
instance is able to host multiple repositories. You can list the IDs of all visible
repositories:

```java
// list all visible Repositories
List<OdmaId> visibleRepositories = session.getRepositoryIds();
System.out.println("Visible Repositories:");
for(OdmaId repoId : visibleRepositories) {
    System.out.println(repoId.toString());
}
```

This will produce the following output:

```
    Visible Repositories:
    sample-repo
```

Please note that the repository ID is an internal value that does not need to be human readable.
You can find the above code in [Lession02_ListVisibleRepositories](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession02_ListVisibleRepositories.java)

## Getting a Repository

Once we have the ID of a Repository, either by configuration or from the list of all
visible repositories, we can get (a description of) this Repository from the session.
We need to create a new `OdmaId` object from the repository ID string `"sample-repo"` and
pass it to the session:

```java
// get the repository by ID
OdmaId repoId = new OdmaId("sample-repo");
OdmaObject repo = session.getRepository(repoId);
```

As described above, all entities in a Repository are handled by OpenDMA as
“objects” – even the repository itself. Every OpenDMA “object”, like our repository,
implements the basic `OdmaObject` interface. This defines some basic features available
for all objects.

## Access to Properties of Objects

Every OpenDMA “object” consists of a set of multiple “properties”. You can access these
properties by their qualified name. Since you know that the repository must have a
property `"DisplayName"` in the namespace `"opendma"`, you can get this property from the
repository object (we will cover how you know that later):

```java
// get the opendma:DisplayName property by qualified name
OdmaQName qnDisplayName = new OdmaQName("opendma", "DisplayName");
OdmaProperty propDisplayName = repo.getProperty(qnDisplayName);
```

There are constant definitions in `OdmaCommonNames` for all well known property and class
names. Instead of creating a new `OdmaQName`, you can also use the predefined constant:

```java
// access by OdmaQName constant for well known property names
repo.getProperty(OdmaCommonNames.PROPERTY_DISPLAYNAME);
```

## Access to the Value of a Property

A property can contain a value of one predefined data type. You can either access the
value as basic Java `Object`

```java
System.out.println("DispName: " + propDisplayName.getValue());
```

or you can use data type specific getter methods

```java
System.out.println("DispName: " + propDisplayName.getString());
```

You can find the above code in [Lession03_GetRepository](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession03_GetRepository.java)

## Runtime type information

In OpenDMA, the layout of every object is defined by its “Class”. This class defines the
exact set of properties by name and type. Every OpenDMA object must have a property named
`"Class"` in the namespace `"opendma"` that contains a reference to the class describing
the object. This class is – again – an OpenDMA object:

```java
// get the opendma:Class of the repository
OdmaObject cls = repo.getProperty(OdmaTypes.PROPERTY_CLASS).getReference();
```

This class object contains some general information

```java
// print out information about the class of the repository
System.out.println("Class of the repository object:");
System.out.println("Name: " + cls.getProperty(OdmaTypes.PROPERTY_NAME).getString() );
System.out.println("ID: " + cls.getProperty(OdmaTypes.PROPERTY_ID).getId() );
System.out.println("System: " + cls.getProperty(OdmaCommonNames.PROPERTY_SYSTEM).getBoolean() );
```

producing the following output:

```
    Class of the repository object:
    Name: XmlRepository
    ID: xml-repository-class
    Sys: true
```
An OpenDMA `Class` references a set of `PropertyInfo` objects that define the name and
data type of every property available on objects of this class:

```java
// get set of PropertyInfos
Iterable<? extends OdmaObject> propInfos = cls.getProperty(OdmaTypes.PROPERTY_PROPERTIES)
                                              .getReferenceIterable();
```

This `opendma:Properties` property is a multi valued Reference property. The Java API
makes it available as `Iterable` over `OdmaObject`:

```java
// print out all PropertyInfos
for(OdmaObject propInfo : propInfos)
{
    System.out.println(propInfo.getProperty(OdmaTypes.PROPERTY_NAME).getString());
}
```

A `Class` in OpenDMA can “extend” another class that is then called the “Super Class” of
that class. The extending class “inherits” all properties defined for the super class. It
can declare additional properties as long as the name is unique.
You have access to the super class with

```java
// get the super class
OdmaObject superCls = cls.getProperty(OdmaTypes.PROPERTY_SUPERCLASS).getReference();
System.out.println("Super class: " + superCls.getProperty(OdmaTypes.PROPERTY_NAME).getString());
```

All classes in an OpenDMA repository form a single rooted tree with the class `Object` as
root. The OpenDMA specification defines a set of system classes, that are extended by the
Adaptor. An OpenDMA Adaptor can define its own classes that cover the entire
functionality while Adaptor independent applications can rely on the basic OpenDMA
classes.

You can find the above code in [Lession04_RuntimeTypeInformation](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession04_RuntimeTypeInformation.java)

The layout of all OpenDMA system classes is defined in the [OpenDMA Specification](https://github.com/OpenDMA/opendma-spec/blob/main/OpenDMA-Spec.md).

To investigate the class hierarchy declared in a concrete repository, you can use the
[OpenDMA GUI](https://github.com/OpenDMA/opendma-swing-gui). It is able to connect to any
OpenDMA repository and displays the class tree and the folder hierarchy of that
repository.

## Class dependant interfaces

For every system defined OpenDMA class, the Java API defines an interface with additional
shortcut getter and setter methods for all properties. Using these shortcuts creates a
much cleaner and more convenient code. When getting a repository from a session, it is
returned as `OdmaRepository` object. It provides getter methods for all properties
defined for a `Repository` in OpenDMA right away:

```java
// get the repository by ID
OdmaId repoId = new OdmaId("sample-repo");
OdmaRepository repo = session.getRepository(repoId);
    
// print out repository info
System.out.println("Repository:");
System.out.println("ID: " + repo.getId());
System.out.println("Name: " + repo.getName());
System.out.println("DispName: " + repo.getDisplayName());
```

When using a getter method for a Reference property, it returns the correct sub interface
instead of the general `OdmaObject` interface.  
For example, the getter method for the property `Class` returns an `OdmaClass` directly:

```java
// get the Class of the Repository
OdmaClass cls = repo.getOdmaClass();
    
// print out information about the class of the repository
System.out.println("Class of the repository object:");
System.out.println("ID: " + cls.getId());
System.out.println("Name: " + cls.getName());
System.out.println("DispName: " + cls.getDisplayName());
System.out.println("Hidden: " + cls.getHidden());
System.out.println("System: " + cls.getSystem());
```

You can find the above code in [Lession05_ClassDependentInterfaces](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession05_ClassDependentInterfaces.java)

## Print all properties of an object

Using all of the above, we can easily create a small method that prints out all
properties of an object. All we need to do is getting the set of properties from the
class of that object. Next, we iterate over this list and get every defined property from
the object. The value is then printed out according to the data type of the property. For
this first simple attempt, we just use the Java built-in String conversion `.toString()`
to print out all data types:

```java
public void printObjectProperties(OdmaObject obj) throws Exception
{
	System.out.println("Object " + obj.getId() + " of class " + obj.getOdmaClass().getQName());
	// get the class of the object
	OdmaClass cls = obj.getOdmaClass();
	// get enumeration of all properties
	Iterable<OdmaPropertyInfo> propInfos = cls.getProperties();
	// iterate over all PropertyInfos
	for(OdmaPropertyInfo propInfo : propInfos)
	{
		OdmaQName propertyName = propInfo.getQName();
		OdmaType dataType = OdmaType.fromNumericId(propInfo.getDataType());
		boolean multiValue = propInfo.isMultiValue();
		// get the property from the object
		OdmaProperty prop = obj.getProperty(propertyName);
		// print property value
		System.out.print("    ");
		System.out.print(propertyName);
		System.out.print(" (" + dataType + ")");
		System.out.print(" [" + (multiValue ? "multi" : "single" ) + "] : ");
		if(!multiValue)
		{
			Object value = prop.getValue();
			if(value == null)
			{
				System.out.println("<null>");
			}
			else
			{
				if(dataType == OdmaType.REFERENCE)
				{
					OdmaObject o = (OdmaObject) value;
					System.out.println(o.getId() + " of class " + o.getOdmaClass().getQName());
				}
				else
				{
					System.out.println(value.toString());
				}
			}
		}
		else
		{
			System.out.println("multivalued");
		}
	}
}
```

You can find a sample program printing out all properties of the Repository object as [Lession06_SimplePrintObjectProperties](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession06_SimplePrintObjectProperties.java)

There is also a more sophisticated implementation that performs proper data conversion,
prints out the data type as descriptive text and also handles multi valued properties.
The details of this are kept for your self study and can be found in
[Lession07_PrintObjectProperties](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession07_PrintObjectProperties.java)

## Predefined and adaptor specific properties

Let’s take a closer look at the properties of our sample repository. When running the
advanced property dump program
[Lession07_PrintObjectProperties](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession07_PrintObjectProperties.java),
it produces the following output:

```
Object sample-repo-object of class tutorial:SampleRepositoryClass
    opendma:Class (REFERENCE) [single] : sample-repo-class of class opendma:Class
    opendma:Aspects (REFERENCE) [multi] : 
    opendma:Id (ID) [single] : sample-repo-object
    opendma:Guid (GUID) [single] : `sample-repo-object` in `sample-repo`
    opendma:Repository (REFERENCE) [single] : sample-repo-object of class tutorial:SampleRepositoryClass
    opendma:Name (STRING) [single] : SampleRepository
    opendma:DisplayName (STRING) [single] : Sample Repository for Tutorials
    opendma:RootClass (REFERENCE) [single] : opendmaClassObject of class opendma:Class
    opendma:RootAspects (REFERENCE) [multi] : 
        opendmaClassAuditStamped of class opendma:Class
        opendmaClassDocument of class opendma:Class
        opendmaClassContentElement of class opendma:Class
        opendmaClassVersionCollection of class opendma:Class
        opendmaClassContainer of class opendma:Class
        opendmaClassContainable of class opendma:Class
        opendmaClassAssociation of class opendma:Class
    opendma:RootFolder (REFERENCE) [single] : sample-folder-root of class tutorial:SampleFolder
    tutorial:RepositoryName (STRING) [single] : SampleRepository
    tutorial:SampleString (STRING) [single] : Some sample string value
    tutorial:SampleInteger (INTEGER) [single] : 123

Inheritance hierarchy:
tutorial:SampleRepositoryClass
opendma:Repository
opendma:Object
```

You can see a set of properties in the `opendma` namespace (from `opendma:Class` through
`opendma:RootFolder`) and three properties in the `tutorial` namespace.

All properties in the `opendma` namespace are guaranteed to be present on every OpenDMA
Repository object. These are also exact those properties, the `OdmaRepository` java
interface provides getter methods for. When writing applications that should be
independent from the actual ECM vendor, you can use these properties to get information
about the repository. The OpenDMA Adaptor is responsible for mapping the correct data to
these properties. 

In this sample repository, you can find three additional properties in the `tutorial`
namespace. These properties are specific to this type of Repository or even to this
repository instance. Here, the general property `opendma:Name` contains the same value as
the ECM vendor specific property `tutorial:RepsoitoryName`. There are many cases where
the properties in the opendma namespace just act as surrogates for native properties.
In other cases, the Adaptor needs to create synthetic properties.

## Inheritance in OpenDMA

Under the list of properties, you can find the inheritance hierarchy for the OpenDMA
class of the Repository object.

```
Inheritance hierarchy:
tutorial:SampleRepositoryClass
opendma:Repository
opendma:Object
```

As in OOP, OpenDMA classes can “extend” one other class and inherit all properties of
this “super” class. Since this parent can itself extend another class, there is a path
from any OpenDMA class to the root of the class hierarchy. In OpenDMA, this root is
always `opendma:Object`.

To inspect this hierarchy path, we can walk this path to the root (until the `SuperClass`
contains `null`) and print for each class the properties that have been declared for this
class: 

```java
// print inheritance hierarchy
System.out.println("Inheritance hierarchy:");
OdmaClass clazz = repo.getOdmaClass();
while(clazz != null)
{
	// print the qualified name of this class
	System.out.println(clazz.getQName());
	// get enumeration of declared properties
	Iterable<OdmaPropertyInfo> propInfos = clazz.getDeclaredProperties();
    // iterate over declared PropertyInfos
    for(OdmaPropertyInfo propInfo : propInfos)
	{
		System.out.println("    "+propInfo.getQName());
	}
	// get next class in hierarchy path
	clazz = clazz.getSuperClass();
}
```

You can find a program printing out the inheritance path of the class of the Repository
as [Lession08_InheritancePath](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession08_InheritancePath.java)

This program will produce the following output:

```
Inheritance hierarchy:
tutorial:SampleRepositoryClass
    tutorial:RepositoryName
    tutorial:SampleString
    tutorial:SampleInteger
opendma:Repository
    opendma:Name
    opendma:DisplayName
    opendma:RootClass
    opendma:RootAspects
    opendma:RootFolder
opendma:Object
    opendma:Class
    opendma:Aspects
    opendma:Id
    opendma:Guid
    opendma:Repository
```

The basic OpenDMA `Object` class declares five Properties that need to be present on
every object in OpenDMA:
- The `Class` of this object for runtime introspection,
- The `Aspects` of this object for runtime introspection,
- the `Id` to uniquely identify this object within the repository,
- the `Guid` combinging the ID with the ID of the repository, and
- the `Repository` the object is located in.

This basic object is extended by the OpenDMA `Repository`, declaring additional
properties that need to be present on every repository, like its name and display name.
This general OpenDMA `Repository` is then extended by the vendor specific
`SampleRepositoryClass`. This class declares all the properties that are only available
in repositories of this ECM vendor. The entire class and inheritance architecture is very
similar to what you know from object oriented programming languages like Java.

Cross cutting concerns are covered with the concept of “Aspects”. An Aspect in OpenDMA
defines a set of properties and can extend another aspect, just like classes.  
A class in OpenDMA “extends” exactly one other class and can “include” zero or more
Aspects. Similar concept are also known in OOP, for example in Java with “interfaces”.

## Inspecting the Inheritance tree

All classes in an OpenDMA repository form a tree with the class `opendma:Object` as root.
Since the Repository object has a reference to this root and we are able to iterate over
sub classes, we can print out the entire inheritance tree. We just need to call this
iterative method for the root of this tree:

```java
protected void iterativePrintClassAndSubclasses(OdmaClass cls, int indent)
{
	for(int i = 0; i < (indent*4); i++)
	{
		System.out.print(" ");
	}
	System.out.println(cls.getQName());
	for(OdmaClass subClass : cls.getSubClasses())
	{
		iterativePrintClassAndSubclasses(subClass,indent+1);
	}
}
```

You can find the program printing out the class tree of our tutorial repository as
[Lession09_PrintClassTree](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession09_PrintClassTree.java)

This program will produce the following output:

```
opendma:Object
    opendma:Class
    opendma:PropertyInfo
    opendma:ChoiceValue
    opendma:Repository
        tutorial:SampleRepositoryClass
    tutorial:SampleFolder
    tutorial:SampleDocument
    tutorial:SampleDataContentElement
    tutorial:SampleReferenceContentElement
    tutorial:SampleVersionCollection
    tutorial:SampleAssociation
    tutorial:SampleContact
    tutorial:SampleEvent
```

Here you can see all basic OpenDMA classes, some of them extended by sample classes in
the `tutorial` namespace. We will work with these classes in the following lessons.

## Document and Content Management

Now that you are familiar with the object oriented approach of OpenDMA,  we can start
with the actual document management OpenDMA is made for.

All this functionality makes use
of the underlying object oriented architecture. There are again some basic classes for
typical document management tasks that are extended by the Adaptor to reflect all
functionality provided by a concrete DMS. Since the DMS world is very heterogeneous,
it is just not possible to represent all these classes in one single rooted tree. Some
DMS vendors make a strict disjunction between files and folders while others allow items in
a repository to be a file and a folder at the same time. To cope with this, OpenDMA uses
the concept of “Aspects”. An Aspect in OpenDMA is similar to an interface in Java. It
declares a set of properties that are added to a class that include this aspect. In
OpenDMA, we have aspects for all basic document management items like Folders, Documents
and so forth.

## Browsing a repository

Lets start with browsing the folder tree. A repository can have a single rooted folder
tree, but it does not have to. There are also repositories out there that do not support
this feature. If a repository supports a folder tree – like our sample repository – there
is a reference to the root folder in the repository object. So we can simply iterate over
the multi-valued `SubFolders` property to print a folder tree:

```java
protected void iterativePrintFolderTree(OdmaFolder folder, int indent)
{
	for(int i = 0; i < (indent*4); i++)
	{
		System.out.print(" ");
	}
	System.out.println(folder.getTitle());
	for(OdmaFolder subFolder : folder.getSubFolders())
	{
		iterativePrintFolderTree(subFolder,indent+1);
	}
}
```

You can find the program calling this method for the root folder as
[Lession10_PrintFolderTree](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession10_PrintFolderTree.java)

This program will produce the following output:

```
ROOT
    TestA
        TestA1
        TestA2
    TestB
        TestB1
        TestB2
    TestC
        TestC1
        TestC2
```

The `RootFolder` and the `SubFolders properties` always return references to objects that
include the `Folder` aspect, represented by java objects implementing the `OdmaFolder`
interface. OpenDMA objects including the `Folder` aspect behave like folders known
from the local file system. In particular, they guarantee the following conditions:
- There is exactly one designated root folder
- The parent of all folders is not `null`, except for the root folder
- The graph of all folders is loop-free under the parent relationship

This allows to perform the above operations without running into dead loops. For some DMS,
there is the need for less restrictive folders. These are realized by the `Container`
aspect in OpenDMA. A `Container` is just an object in a repository that can “contain”
other objects. There are no constraints about the layout of the graph formed by
this relationship. Especially not the loop freeness. The only constraint for Containers
is that the contained object have to incorporate the `Containable` aspect.

## Browsing the content of a folder

With the above code, we just iterate over the folders, but not their content. The folder
hierarchy in an OpenDMA repository constitutes a structure, where arbitrary objects can
be linked in. In contrast to folders in the local file system, any object can be linked
into these folders (as long as it incorporates the `Containable` aspect). An object can
also be linked into multiple folders and even multiple times into the same folder.

To see the contained objects in our sample repository, we need to extend the above code
to additionally iterate over all containees:

```java
protected void iterativePrintFolderContent(OdmaFolder folder, int indent)
{
	StringBuffer indentStr = new StringBuffer(indent*4);
	for(int i = 0; i < (indent*4); i++)
	{
		indentStr.append(" ");
	}
	System.out.println(indentStr.toString() + "-" + folder.getTitle());
    for(OdmaContainable containable : folder.getContainees())
    {
        System.out.println(indentStr.toString() + 
		                   "    = ID " + containable.getId() + 
						   " [" + containable.getOdmaClass().getQName() + "]");
    }
	for(OdmaFolder subFolder : folder.getSubFolders())
	{
		iterativePrintFolderContent(subFolder,indent+1);
	}
}
```

You can find this program as
[Lession11_PrintFolderContent](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession11_PrintFolderContent.java)

It creates the following output for our sample repository:

```
-ROOT
    -TestA
        = ID sample-document-a1 [tutorial:SampleDocument]
        = ID sample-document-a2 [tutorial:SampleDocument]
        = ID contact-1 [tutorial:SampleContact]
        = ID event-1 [tutorial:SampleEvent]
        -TestA1
        -TestA2
    -TestB
        = ID sample-document-b1 [tutorial:SampleDocument]
        = ID sample-document-b2 [tutorial:SampleDocument]
        -TestB1
        -TestB2
    -TestC
        -TestC1
        -TestC2
```

There are not only files being linked into folders (the `SampleDocument` objects), but
also contacts (`SampleContact`) and events (`SampleEvent`).

Some ECM systems, like Alfresco or FileNet P8, allow this link between a Folder and its
content to be explicitly modeled. This is covered by OpenDMA with the concept of
“Associations”. We leave this advanced concept to the reader for self study with
[Lession16_PrintFolderAssociations](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession16_PrintFolderAssociations.java)

## Working with Documents

The entities you typically work with in an ECM system are represented by the
`Document` aspect. Such a `Document` can be seen as an evolved version of a file. Before
digging deeper into the details of `Document`s, let’s take a look at their properties.
The program
[Lession12_PrintSampleDocumentProperties](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession12_PrintSampleDocumentProperties.java)
is a copy of
[Lession07_PrintObjectProperties](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession07_PrintObjectProperties.java)
that retrieves our sample Document by its ID (`sample-document-a1`) and prints out all
properties:

```
Object sample-document-a1 of class tutorial:SampleDocument
    opendma:Class (REFERENCE) [single] : sample-document-class of class opendma:Class
    opendma:Aspects (REFERENCE) [multi] : 
    opendma:Id (ID) [single] : sample-document-a1
    opendma:Guid (GUID) [single] : `sample-document-a1` in `sample-repo`
    opendma:Repository (REFERENCE) [single] : sample-repo-object of class tutorial:SampleRepositoryClass
    opendma:Title (STRING) [single] : Some Word file
    opendma:Version (STRING) [single] : 2.1
    opendma:VersionCollection (REFERENCE) [single] : sample-versioncollection-a1 of class tutorial:SampleVersionCollection
    opendma:VersionIndependentId (ID) [single] : <null>
    opendma:VersionIndependentGuid (GUID) [single] : <null>
    opendma:ContentElements (REFERENCE) [multi] : 
        sample-document-a1-dacoel of class tutorial:SampleDataContentElement
    opendma:CombinedContentType (STRING) [single] : application/msword
    opendma:PrimaryContentElement (REFERENCE) [single] : sample-document-a1-dacoel of class tutorial:SampleDataContentElement
    opendma:CheckedOut (BOOLEAN) [single] : false
    opendma:CheckedOutAt (DATETIME) [single] : <null>
    opendma:CheckedOutBy (STRING) [single] : <null>
    opendma:ContainedIn (REFERENCE) [multi] : 
        sample-folder-a of class tutorial:SampleFolder
    opendma:ContainedInAssociations (REFERENCE) [multi] : 
        sample-association-a1 of class tutorial:SampleAssociation
    opendma:CreatedAt (DATETIME) [single] : Fri Jan 01 00:00:00 CET 2010
    opendma:CreatedBy (STRING) [single] : SYSTEM
    opendma:LastModifiedAt (DATETIME) [single] : Fri Jan 01 00:00:00 CET 2010
    opendma:LastModifiedBy (STRING) [single] : SYSTEM
```

Investigating these properties, you will notice that there is no filename as you would
expect for files. Instead, a `Document` has an human readable “Title”. To classify the
data it uses the “Content Type” (e.g. `"application/msword"`) instead of a file extension
(e.g. `.doc`). If the `Document` is linked into one or more folders, these
`Association` objects contain a path-safe name. In our sample repository, the
`Document` has the title `"Some Word file"` and the Content-Type `"application/msword"`.
The association that links this document into the folder `"TestA"` has the name
`"SomeWordfile.doc"`. This allows for the construction of a “path” as known from file
systems.

In contrast to files, a `Document` is a higher level concept. It can contain multiple
data parts, represented as `ContentElement` objects. This allows a scanned document to
consist of multiple TIFF files where each image represents one page. To access the data of a
`Document`, we need to investigate these `ContentElement`s:

```java
public void printContentElements(OdmaDocument doc)
{
	System.out.println("Content Elements of Document \"" + doc.getTitle() + "\":");
	for(OdmaContentElement contElem : doc.getContentElements())
	{
		System.out.println("  Pos " + contElem.getPosition() + 
		                   " of type " + contElem.getContentType() + 
						   " as " + contElem.getOdmaClass().getQName() );
	}
}
```

The program
[Lession13_PrintDocumentContentElements](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession13_PrintDocumentContentElements.java)
runs this code on our sample movie document (`sample-document-a2`) and produces this
output:

```
Content Elements of Document "Some Movie":
  Pos 0 of type video/mp4 as tutorial:SampleDataContentElement
  Pos 1 of type video/mp4 as tutorial:SampleDataContentElement
  Pos 2 of type video/mp4 as tutorial:SampleReferenceContentElement
```

You can see this high level “logical” document consisting of three “physical” MPEG4
content elements.

OpenDMA does not only allow multiple data parts for one document, it
also supports different provisioning methods of the data. In fact, it provides a framework
for implementation specific provision methods. The core architecture defines two
aspects extending the `ContentElement` aspect: the `DataContentElement` for a stream based
provisioning of octet data and the `ReferenceContentElement` for URI based references to
external data. To retrieve the actual data of a `ContentElement`, we need to investigate
the type of the `ContentElement` first and then deal with this `ContentElement` depending
on its content disposition:

```java
System.out.println("  Pos " + contElem.getPosition() +
                   " of type " + contElem.getContentType() + 
				   " as " + contElem.getOdmaClass().getQName() );
if(contElem.instanceOf(OdmaCommonNames.CLASS_DATACONTENTELEMENT))
{
	System.out.println("    Data ContentElement");
	System.out.println("    Size: "+((OdmaDataContentElement)contElem).getSize());
	// ...we will deal with data content later
}
else if(contElem.instanceOf(OdmaCommonNames.CLASS_REFERENCECONTENTELEMENT))
{
	System.out.println("    Reference ContentElement");
	System.out.println("    Location: "+((OdmaReferenceContentElement)contElem).getLocation());
}
else
{
	System.out.println("    Unknown sub-type of ContentElement.");
	System.out.println("    Don't know how to retrieve the data.");
}
```

You can find the above code in the program
[Lession14_RetrieveDataFromContentElements](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession14_RetrieveDataFromContentElements.java)
producing the following output:
```
Content Elements of Document "Some Movie":
  Pos 0 of type video/mp4 as tutorial:SampleDataContentElement
    Data ContentElement
    Size: 1327
    Conent: 0x89 0x50 0x4e 0x47 0xd 0xa 0x1a 0xa...
  Pos 1 of type video/mp4 as tutorial:SampleDataContentElement
    Data ContentElement
    Size: 1327
    Conent: 0x89 0x50 0x4e 0x47 0xd 0xa 0x1a 0xa...
  Pos 2 of type video/mp4 as tutorial:SampleReferenceContentElement
    Reference ContentElement
    Location: http://media.example.com/path/to/Scene3.mp4
```

You can see the reference content element on position 2 just containing a URI to the
location of the content. Please note that such a reference does not guarantee the
existence of data at this location nor that the data is of the proclaimed content type.

The `DataContentElement`s on position 0 and 1 instead provide their data as stream. To
access the data, you need to read the value of the `Content` property that is of the
special data type `CONTENT`. This property can be empty (`null`). If not, its value is
represented in Java as `OdmaContent`.  This interface provides methods to get a
`java.io.InputStream` to access the data. This stream needs to be closed after usage.
The following code is used in the above program to print out the hex-dump excerpt of
the binary content:

```java
OdmaContent dataContent = ((OdmaDataContentElement)contElem).getContent();
if(dataContent != null)
{
    System.out.print("    Conent:");
    InputStream inDataContent = dataContent.getStream();
    try
    {
        if(inDataContent != null)
        {
            int cnt = 0;
            int data = 0;
            while( (cnt++ < 10) && ((data= inDataContent.read())>0))
            {
                System.out.print(" 0x");
                System.out.print(Integer.toHexString(data));
            }
            System.out.println("...");
        }
    }
    catch(IOException ioe)
    {
        System.out.print("ERROR READING STREAM");
    }
    finally
    {
        try
        {
            inDataContent.close();
        }
        catch(IOException ioe)
        {
            System.out.print("ERROR CLOSING STREAM");
        }
    }
}
else
{
    System.out.println("    Conent: NULL");
}
```

## Working with document versions

A `Document` in OpenDMA can be under version control. In this case, the document provides
a reference to a `VersionCollection` object that keeps track of the history of the
document. It provides references to the latest version, the released version and the
in-progress version. These three versions are defined as follows:

Latest version:  
The most recent version of this document that is not currently edited.

Released version:  
The most recent version of this document that has been marked as released.

In-progress version:  
The working copy of this document, if this document is currently edited. A repository can
create a copy of the latest version when a user starts editing a document. This working
copy becomes the new latest version once the working copy is “checked in”. Otherwise,
it is discarded if the editing process is canceled.

The program
[Lession15_PrintDocumentVersions](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession15_PrintDocumentVersions.java)
checks for the `VersionCollection` of our sample word document and prints out all versions
of this document controlled by that version collection. It produces the following output:

```
Content Elements of Document "Some Word file":
  Latest: 2.1 ID: sample-document-a1
  Released: 2.0 ID: sample-document-a1-v20
  InProgress: null
  All Versions:
    1.0 ID: sample-document-a1-v10
        Title: Soeme File
    1.1 ID: sample-document-a1-v11
        Title: Some file
    1.2 ID: sample-document-a1-v12
        Title: Some Word File
    1.3 ID: sample-document-a1-v13
        Title: Some Word file
    2.0 ID: sample-document-a1-v20
        Title: Some Word file
    2.1 ID: sample-document-a1
        Title: Some Word file
```

You can see in this printout, that version control is not only applied to the content of
a document, but also to it’s properties. So you can see the value of the title being
modified across the version history.










## Browsing the content of a folder

With the above code, we just iterate over the folders, but not their content. The folder
hierarchy in an OpenDMA repository constitutes a structure, where arbitrary objects can
be linked in. In contrast to folders in the local file system, any object can be linked
into these folders (as long as it incorporates the `Containable` aspect). An object can
also be linked into multiple folders and even multiple times into the same folder. A
folder holds a set of “Associations” that represent the link between a folder and some
object. Each association has its own name, so that it is possible to browse the combined
structure of folders and linked objects by name, even if the linked object does not have
a name. With the assignment of a unique name, it is also possible to resolve an object
linked into the folder structure by path.

To see the contained objects in our sample repository, we need to extend the above code
to additionally iterate over all associations and print out the name of this association
together with the id of the contained object:

```java
protected void iterativePrintFolderContent(OdmaFolder folder, int indent)
{
	StringBuffer indentStr = new StringBuffer(indent*4);
	for(int i = 0; i < (indent*4); i++)
	{
		indentStr.append(" ");
	}
	System.out.println(indentStr.toString() + "-" + folder.getTitle());
	for(OdmaAssociation assoc : folder.getAssociations())
	{
		System.out.print(indentStr.toString() + "    =" + assoc.getName());
		OdmaContainable containee = assoc.getContainable();
		System.out.println(" --> ID " + containee.getId() + " [" + containee.getOdmaClass().getQName() + "]");
	}
	for(OdmaFolder subFolder : folder.getSubFolders())
	{
		iterativePrintFolderContent(subFolder,indent+1);
	}
}
```

You can find this program as
[Lession11_PrintFolderContent](https://github.com/OpenDMA/opendma-java-tutorial/blob/main/src/main/java/org/opendma/tutorial/Lession11_PrintFolderContent.java)

It creates the following output for our sample repository:

```
-ROOT
    -TestA
        =SomeWordfile.doc --> ID sample-document-a1 [tutorial:SampleDocument]
        =SomeMovie.mp4 --> ID sample-document-a2 [tutorial:SampleDocument]
        =Sixpack, Joe --> ID contact-1 [tutorial:SampleContact]
        =Christmas --> ID event-1 [tutorial:SampleEvent]
        -TestA1
        -TestA2
    -TestB
        =AnotherWordfile.doc --> ID sample-document-b1 [tutorial:SampleDocument]
        =AnotherMovie.mp4 --> ID sample-document-b2 [tutorial:SampleDocument]
        -TestB1
        -TestB2
    -TestC
        -TestC1
        -TestC2
```

There are not only files being linked into folders (the `SampleDocument` objects), but
also contacts (`SampleContact`) and events (`SampleEvent`).

