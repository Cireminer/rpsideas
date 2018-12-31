package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickPlantSequence extends PieceTrick {

    private SpellParam position;
    private SpellParam target;
    private SpellParam maxBlocks;

    public PieceTrickPlantSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
        addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocks);

        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 8));
        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 8));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.getParamValue(context, position);
        Vector3 targetVal = this.getParamValue(context, target);
        Double maxBlocksVal = this.<Double>getParamValue(context, maxBlocks);
        int maxBlocksInt = maxBlocksVal.intValue();

        if (positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        int len = (int) targetVal.mag();
        Vector3 targetNorm = targetVal.copy().normalize();
        for (int i = 0; i < Math.min(len, maxBlocksInt); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

            if (!context.isInRadius(blockVec))
                throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

            BlockPos pos = blockVec.toBlockPos();
            PieceTrickPlant.plantPlant(context.caster, context.caster.world, pos, context.getTargetSlot());
        }

        return null;
    }

}
