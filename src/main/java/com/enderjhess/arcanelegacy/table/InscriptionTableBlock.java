package com.enderjhess.arcanelegacy.table;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

/** Mesa de Inscrição: onde o jogador junta runas para montar um feitiço. */
public class InscriptionTableBlock extends BaseEntityBlock {
    public static final MapCodec<InscriptionTableBlock> CODEC = simpleCodec(InscriptionTableBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<TablePhase> PHASE = EnumProperty.create("phase", TablePhase.class);

    public InscriptionTableBlock(Properties properties) {
        super(properties.lightLevel(state -> state.getValue(PHASE).lightLevel()));
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PHASE, TablePhase.IDLE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PHASE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // A faixa roxa fica virada para quem colocou a mesa.
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InscriptionTableBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof InscriptionTableBlockEntity table) {
            player.openMenu(table);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof InscriptionTableBlockEntity table) {
            Containers.dropContents(level, pos, table);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;
        switch (state.getValue(PHASE)) {
            case IDLE -> {
            }
            case CRAFTING -> {
                // Faíscas roxas subindo do círculo.
                for (int i = 0; i < 2; i++) {
                    level.addParticle(ParticleTypes.WITCH,
                            x + (random.nextDouble() - 0.5) * 0.8, y + 0.05, z + (random.nextDouble() - 0.5) * 0.8,
                            0, 0.02, 0);
                }
                if (random.nextInt(3) == 0) {
                    level.addParticle(ParticleTypes.ENCHANT, x, y + 0.6, z,
                            (random.nextDouble() - 0.5) * 1.5, -0.4, (random.nextDouble() - 0.5) * 1.5);
                }
            }
            case READY -> {
                // Feixe de luz saindo do centro.
                for (int i = 0; i < 4; i++) {
                    level.addParticle(ParticleTypes.END_ROD,
                            x + (random.nextDouble() - 0.5) * 0.15, y + random.nextDouble() * 0.3,
                            z + (random.nextDouble() - 0.5) * 0.15, 0, 0.12 + random.nextDouble() * 0.08, 0);
                }
                level.addParticle(ParticleTypes.WITCH,
                        x + (random.nextDouble() - 0.5) * 0.8, y + 0.05, z + (random.nextDouble() - 0.5) * 0.8,
                        0, 0.02, 0);
            }
        }
    }
}
