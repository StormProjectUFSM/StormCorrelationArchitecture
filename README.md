# CORRELATION STORM TOPOLOGY ON A POLITICS APPROACH

Development beginning: 03/2017 

Last update: 04/2017


**Working group ->** 

Universidade Federal de Santa Maria (UFSM)

Grupo de REdes e Computação Aplicada (GRECA)


**Searchers ->**

Nilton Camargo Batista da Silva (nbatista@inf.ufsm.br)

Vinícius Fülber Garcia (vfulber@inf.ufsm.br)

Carlos Raniery P. dos Santos (csantos@inf.ufsm.br)

## STORM PROGRAMMING WORK LIST

### DONE

* [Storm scripts for programming](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/tree/master/shortcuts)

* [Politics XML Structure](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/tree/master/src/jvm/storm/starter/MetadataBase)

	- [Manipulation API](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/blob/master/src/jvm/storm/starter/AlgorithmBase/PoliticsXML.java)

* [Correlation XML Metadata Structure](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/tree/master/src/jvm/storm/starter/MetadataBase)

	- [Manipulation API](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/blob/master/src/jvm/storm/starter/AlgorithmBase/CorrelationXML.java)

* [Correlation Bolts](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/tree/master/src/jvm/storm/starter/CorrelationBase)

	- [Compression - Time Trigger](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/blob/master/src/jvm/storm/starter/CorrelationBase/CompressionBolt.java)

	- [Counter - Time Trigger](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/blob/master/src/jvm/storm/starter/CorrelationBase/CounterBolt.java)

	- [Filter - Time Trigger](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/blob/master/src/jvm/storm/starter/CorrelationBase/FilterBolt.java)

* [Tests Set](https://github.com/StormProjectUFSM/StormCorrelationArchitecture/tree/master/src/jvm/storm/starter/TestsBase)

### TO DO

* Correlation Bolts

	- Compression - Event Trigger

	- Counter - Event Trigger

	- Filter - Event Trigger

* Action Bolts

	- Log Bolt

	- E-mail Bolt

## ARCHITECTURE WORK LIST

### DONE

* Storm Ambient

	- Installation and configuration

### TO DO

* Kafka Integration

	- Flow topics creation

* Politics System Integration

	- Create definition file for topologies
