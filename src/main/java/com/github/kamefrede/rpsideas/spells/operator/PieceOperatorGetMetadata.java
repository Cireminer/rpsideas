package com.github.kamefrede.rpsideas.spells.operator;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetMetadata extends PieceOperator {
    public PieceOperatorGetMetadata(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.COMPLEXITY, 1);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(!context.caster.world.isRemote){
            if(context.targetSlot != 0 && !context.caster.inventory.getStackInSlot(context.targetSlot).isEmpty()){
                return context.caster.inventory.getStackInSlot(context.targetSlot).getMetadata() * 1D;
            }
        }
        return 0D;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
