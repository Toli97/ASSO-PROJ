package asso.model.knowledgeSources.generators

import asso.model.Blackboard
import asso.model.knowledgeSources.KnowledgeSource
import asso.model.objects.ProcessingStage

abstract class Generator(blackboard: Blackboard, chain: Vector[ProcessingStage]) extends KnowledgeSource(blackboard)
